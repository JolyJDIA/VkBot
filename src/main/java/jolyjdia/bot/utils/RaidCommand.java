package jolyjdia.bot.utils;

import com.google.common.collect.Maps;
import jolyjdia.api.command.Command;
import jolyjdia.api.permission.PermissionManager;
import jolyjdia.api.scheduler.RoflanRunnable;
import jolyjdia.api.storage.Chat;
import jolyjdia.api.storage.User;
import jolyjdia.api.utils.StringBind;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RaidCommand extends Command {
    private final Map<Integer, RoflanRunnable> raids = Maps.newHashMap();

    @NonNls private static final String CHELIBOSI =
            "\uD83D\uDE00\uD83D\uDE01\uD83D\uDE02\uD83E\uDD23\uD83D\uDE03\uD83D\uDE04\uD83D\uDE05\uD83D\uDE06\uD83D\uDE09\uD83D\uDE0A\uD83D\uDE0B\uD83D\uDE0E\uD83D\uDE0D\uD83D\uDE18\uD83D\uDE17\uD83D\uDE06\uD83D\uDE19\uD83D\uDE1A☺\uD83D\uDE42\uD83E\uDD29\uD83E\uDD17\uD83E\uDD28\uD83E\uDD14\uD83D\uDE10\uD83D\uDE11\uD83D\uDE36\uD83D\uDE44\uD83D\uDE0F\uD83D\uDE1C\uD83E\uDD10\uD83D\uDE14\uD83E\uDD2C\uD83D\uDE21\uD83D\uDE37\uD83D\uDE31\uD83D\uDE2C\uD83D\uDE27\uD83E\uDD24☹\uD83D\uDE33\uD83E\uDD22\uD83E\uDD22\uD83E\uDD27\uD83D\uDE07\uD83D\uDE08\uD83D\uDE08\uD83D\uDC7F\uD83D\uDE40\uD83E\uDD16\uD83D\uDC7D\uD83D\uDC80☠\uD83D\uDC7B\uD83D\uDE3B\uD83D\uDE39\uD83D\uDE38\uD83D\uDE3B\uD83D\uDE4A\uD83D\uDE49\uD83D\uDE48\uD83D\uDC69\uD83E\uDDD2\uD83D\uDC74\uD83D\uDC66\uD83D\uDC68\u200D⚕\uD83D\uDC68\u200D\uD83C\uDF93\uD83D\uDC68\u200D\uD83C\uDF73\uD83D\uDC68\u200D✈\uD83E\uDDD5\uD83D\uDC72\uD83D\uDC73\u200D♀\uD83D\uDC73\uD83E\uDD34\uD83D\uDD75\uD83D\uDC82\uD83D\uDC69\u200D\uD83C\uDFA4\uD83D\uDC68\u200D\uD83C\uDFA4\uD83D\uDC69\u200D\uD83D\uDCBB\uD83D\uDC68\u200D\uD83D\uDCBB\uD83D\uDC86\uD83D\uDE4B\u200D♂\uD83E\uDDD8\uD83E\uDDD7\uD83D\uDC6F\uD83D\uDC86\u200D♂\uD83C\uDFC3\u200D♀\uD83C\uDFC3\u200D♀\uD83D\uDECC⛷\uD83E\uDD38\u200D♂\uD83C\uDFCB\u200D♀\uD83C\uDFC4\uD83D\uDEB5\uD83E\uDD39\uD83E\uDD3E\uD83E\uDD3C\uD83E\uDD39\uD83E\uDD3E\uD83C\uDFC4\uD83D\uDC91\uD83D\uDC97\uD83E\uDD1A\uD83D\uDC85✋✋\uD83D\uDD95\uD83D\uDC46\uD83D\uDC46☝\uD83D\uDC49\uD83D\uDC4A\uD83E\uDD1F\uD83D\uDE4C✍\uD83E\uDD1E\uD83D\uDD96\uD83D\uDC4F\uD83D\uDC43\uD83D\uDC4D\uD83E\uDD19\uD83E\uDD1C\uD83E\uDD1D\uD83D\uDC95\uD83D\uDC9D\uD83D\uDCA6\uD83D\uDCA8\uD83D\uDCA5\uD83D\uDCA3\uD83D\uDCA4\uD83D\uDC8C\uD83E\uDDE3\uD83D\uDC51\uD83D\uDCAD\uD83D\uDECD\uD83C\uDF92\uD83D\uDC60\uD83D\uDC54\uD83D\uDCAC\uD83E\uDD92\uD83D\uDC3F\uD83E\uDD89\uD83D\uDC13\uD83D\uDC13\uD83D\uDC13\uD83D\uDC13\uD83D\uDC33\uD83D\uDC1A\uD83D\uDC1B\uD83D\uDC0C\uD83E\uDD91\uD83D\uDD78\uD83E\uDD82\uD83D\uDD77\uD83D\uDD78\uD83D\uDC90\uD83D\uDCAE\uD83C\uDFF5\uD83E\uDD90\uD83E\uDD95\uD83D\uDC27\uD83D\uDC24\uD83C\uDF54\uD83E\uDD55\uD83E\uDD5D\uD83C\uDF73\uD83C\uDF7F\uD83C\uDF44\uD83C\uDF5E\uD83C\uDF8F\uD83C\uDF8D\uD83C\uDF8D\uD83C\uDF8F\uD83C\uDF8F\uD83C\uDFC9\uD83C\uDFC9\uD83C\uDFC9\uD83C\uDFD2\uD83C\uDFD2\uD83C\uDFD1\uD83C\uDF9F\uD83C\uDF9F\uD83C\uDF9F\uD83C\uDF8E\uD83C\uDF8E\uD83C\uDF91\uD83C\uDFC5\uD83C\uDFC5\uD83C\uDFC9\uD83C\uDFF8\uD83C\uDFF8\uD83C\uDF0F\uD83C\uDF0F\uD83D\uDDFA\uD83D\uDDFA\uD83E\uDD4C\uD83E\uDD4C\uD83C\uDFB4⛺⛲⛲⛩⛩\uD83C\uDFEF\uD83C\uDFEF\uD83C\uDFEF\uD83C\uDFE6♨\uD83D\uDE86\uD83C\uDFAA\uD83D\uDEF8\uD83C\uDF06\uD83D\uDEE9\uD83D\uDECE\uD83D\uDD60\uD83D\uDD5C\uD83D\uDD61\uD83D\uDD65\uD83D\uDD60\uD83D\uDD53\uD83D\uDD52\uD83D\uDEBD\uD83D\uDEBD\uD83D\uDCBA⛴\uD83D\uDECB\uD83C\uDF29\uD83C\uDF1B\uD83C\uDF14\uD83C\uDF15\uD83C\uDF20\uD83C\uDF1E\uD83C\uDF17\uD83C\uDF24\uD83C\uDF08\uD83D\uDD09\uD83D\uDD07\uD83D\uDCFB\uD83C\uDFB9\uD83D\uDCE2\uD83D\uDCEF\uD83C\uDFA4\uD83D\uDD0C\uD83D\uDCDC\uD83D\uDD0E\uD83D\uDDB2\uD83D\uDD2D\uD83D\uDCDA\uD83D\uDCB8\uD83C\uDFF7\uD83D\uDCF0\uD83D\uDD6F\uD83D\uDCD8\uD83D\uDCEE✏\uD83D\uDD8B\uD83D\uDD8A\uD83D\uDCCB✂\uD83D\uDCCF\uD83D\uDCCD⛏⚒\uD83D\uDC8A\uD83D\uDEB9\uD83D\uDEB0\uD83C\uDFE7\uD83D\uDEB3\uD83D\uDEC3⚠\uD83D\uDEB7\uD83D\uDD1E\uD83D\uDEAF\uD83D\uDD02\uD83D\uDD4E♈♒\uD83D\uDD3D\uD83C\uDFA6©❇✖\uD83D\uDCDB\uD83D\uDD06\uD83D\uDCF3\uD83D\uDCF4⃣\uD83C\uDD93\uD83D\uDD20\uD83D\uDCAF\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE39\uD83C\uDE1A\uD83C\uDE51\uD83C\uDE33\uD83C\uDE34\uD83D\uDD38\uD83D\uDD37⬛\uD83C\uDDE6\uD83C\uDDEC\uD83D\uDEA9\uD83C\uDFF3\u200D\uD83C\uDF08\uD83C\uDFC3\u200D♀\uD83D\uDE00\uD83D\uDE01\uD83D\uDE02\uD83E\uDD23\uD83D\uDE03\uD83D\uDE04\uD83D\uDE05\uD83D\uDE06\uD83D\uDE09\uD83D\uDE0A\uD83D\uDE0B\uD83D\uDE0E\uD83D\uDE0D\uD83D\uDE18\uD83D\uDE17\uD83D\uDE06\uD83D\uDE19\uD83D\uDE1A☺\uD83D\uDE42\uD83E\uDD29\uD83E\uDD17\uD83E\uDD28\uD83E\uDD14\uD83D\uDE10\uD83D\uDE11\uD83D\uDE36\uD83D\uDE44\uD83D\uDE0F\uD83D\uDE1C\uD83E\uDD10\uD83D\uDE14\uD83E\uDD2C\uD83D\uDE21\uD83D\uDE37\uD83D\uDE31\uD83D\uDE2C\uD83D\uDE27\uD83E\uDD24☹\uD83D\uDE33\uD83E\uDD22\uD83E\uDD22\uD83E\uDD27\uD83D\uDE07\uD83D\uDE08\uD83D\uDE08\uD83D\uDC7F\uD83D\uDE40\uD83E\uDD16\uD83D\uDC7D\uD83D\uDC80☠\uD83D\uDC7B\uD83D\uDE3B\uD83D\uDE39\uD83D\uDE38\uD83D\uDE3B\uD83D\uDE4A\uD83D\uDE49\uD83D\uDE48\uD83D\uDC69\uD83E\uDDD2\uD83D\uDC74\uD83D\uDC66\uD83D\uDC68\u200D⚕\uD83D\uDC68\u200D\uD83C\uDF93\uD83D\uDC68\u200D\uD83C\uDF73\uD83D\uDC68\u200D✈\uD83E\uDDD5\uD83D\uDC72\uD83D\uDC73\u200D♀\uD83D\uDC73\uD83E\uDD34\uD83D\uDD75\uD83D\uDC82\uD83D\uDC69\u200D\uD83C\uDFA4\uD83D\uDC68\u200D\uD83C\uDFA4\uD83D\uDC69\u200D\uD83D\uDCBB\uD83D\uDC68\u200D\uD83D\uDCBB\uD83D\uDC86\uD83D\uDE4B\u200D♂\uD83E\uDDD8\uD83E\uDDD7\uD83D\uDC6F\uD83D\uDC86\u200D♂\uD83C\uDFC3\u200D♀\uD83C\uDFC3\u200D♀\uD83D\uDECC⛷\uD83E\uDD38\u200D♂\uD83C\uDFCB\u200D♀\uD83C\uDFC4\uD83D\uDEB5\uD83E\uDD39\uD83E\uDD3E\uD83E\uDD3C\uD83E\uDD39\uD83E\uDD3E\uD83C\uDFC4\uD83D\uDC91\uD83D\uDC97\uD83E\uDD1A\uD83D\uDC85✋✋\uD83D\uDD95\uD83D\uDC46\uD83D\uDC46☝\uD83D\uDC49\uD83D\uDC4A\uD83E\uDD1F\uD83D\uDE4C✍\uD83E\uDD1E\uD83D\uDD96\uD83D\uDC4F\uD83D\uDC43\uD83D\uDC4D\uD83E\uDD19\uD83E\uDD1C\uD83E\uDD1D\uD83D\uDC95\uD83D\uDC9D\uD83D\uDCA6\uD83D\uDCA8\uD83D\uDCA5\uD83D\uDCA3\uD83D\uDCA4\uD83D\uDC8C\uD83E\uDDE3\uD83D\uDC51\uD83D\uDCAD\uD83D\uDECD\uD83C\uDF92\uD83D\uDC60\uD83D\uDC54\uD83D\uDCAC\uD83E\uDD92\uD83D\uDC3F\uD83E\uDD89\uD83D\uDC13\uD83D\uDC13\uD83D\uDC13\uD83D\uDC13\uD83D\uDC33\uD83D\uDC1A\uD83D\uDC1B\uD83D\uDC0C\uD83E\uDD91\uD83D\uDD78\uD83E\uDD82\uD83D\uDD77\uD83D\uDD78\uD83D\uDC90\uD83D\uDCAE\uD83C\uDFF5\uD83E\uDD90\uD83E\uDD95\uD83D\uDC27\uD83D\uDC24\uD83C\uDF54\uD83E\uDD55\uD83E\uDD5D\uD83C\uDF73\uD83C\uDF7F\uD83C\uDF44\uD83C\uDF5E\uD83C\uDF8F\uD83C\uDF8D\uD83C\uDF8D\uD83C\uDF8F\uD83C\uDF8F\uD83C\uDFC9\uD83C\uDFC9\uD83C\uDFC9\uD83C\uDFD2\uD83C\uDFD2\uD83C\uDFD1\uD83C\uDF9F\uD83C\uDF9F\uD83C\uDF9F\uD83C\uDF8E\uD83C\uDF8E\uD83C\uDF91\uD83C\uDFC5\uD83C\uDFC5\uD83C\uDFC9\uD83C\uDFF8\uD83C\uDFF8\uD83C\uDF0F\uD83C\uDF0F\uD83D\uDDFA\uD83D\uDDFA\uD83E\uDD4C\uD83E\uDD4C\uD83C\uDFB4⛺⛲⛲⛩⛩\uD83C\uDFEF\uD83C\uDFEF\uD83C\uDFEF\uD83C\uDFE6♨\uD83D\uDE86\uD83C\uDFAA\uD83D\uDEF8\uD83C\uDF06\uD83D\uDEE9\uD83D\uDECE\uD83D\uDD60\uD83D\uDD5C\uD83D\uDD61\uD83D\uDD65\uD83D\uDD60\uD83D\uDD53\uD83D\uDD52\uD83D\uDEBD\uD83D\uDEBD\uD83D\uDCBA⛴\uD83D\uDECB\uD83C\uDF29\uD83C\uDF1B\uD83C\uDF14\uD83C\uDF15\uD83C\uDF20\uD83C\uDF1E\uD83C\uDF17\uD83C\uDF24\uD83C\uDF08\uD83D\uDD09\uD83D\uDD07\uD83D\uDCFB\uD83C\uDFB9\uD83D\uDCE2\uD83D\uDCEF\uD83C\uDFA4\uD83D\uDD0C\uD83D\uDCDC\uD83D\uDD0E\uD83D\uDDB2\uD83D\uDD2D\uD83D\uDCDA\uD83D\uDCB8\uD83C\uDFF7\uD83D\uDCF0\uD83D\uDD6F\uD83D\uDCD8\uD83D\uDCEE✏\uD83D\uDD8B\uD83D\uDD8A\uD83D\uDCCB✂\uD83D\uDCCF\uD83D\uDCCD⛏⚒\uD83D\uDC8A\uD83D\uDEB9\uD83D\uDEB0\uD83C\uDFE7\uD83D\uDEB3\uD83D\uDEC3⚠\uD83D\uDEB7\uD83D\uDD1E\uD83D\uDEAF\uD83D\uDD02\uD83D\uDD4E♈♒\uD83D\uDD3D\uD83C\uDFA6©❇✖\uD83D\uDCDB\uD83D\uDD06\uD83D\uDCF3\uD83D\uDCF4⃣\uD83C\uDD93\uD83D\uDD20\uD83D\uDCAF\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE39\uD83C\uDE1A\uD83C\uDE51\uD83C\uDE33\uD83C\uDE34\uD83D\uDD38\uD83D\uDD37⬛\uD83C\uDDE6\uD83C\uDDEC\uD83D\uDEA9\uD83C\uDFF3\u200D\uD83C\uDF08\uD83C\uDFC3\u200D♀\uD83D\uDE00\uD83D\uDE01\uD83D\uDE02\uD83E\uDD23\uD83D\uDE03\uD83D\uDE04\uD83D\uDE05\uD83D\uDE06\uD83D\uDE09\uD83D\uDE0A\uD83D\uDE0B\uD83D\uDE0E\uD83D\uDE0D\uD83D\uDE18\uD83D\uDE17\uD83D\uDE06\uD83D\uDE19\uD83D\uDE1A☺\uD83D\uDE42\uD83E\uDD29\uD83E\uDD17\uD83E\uDD28\uD83E\uDD14\uD83D\uDE10\uD83D\uDE11\uD83D\uDE36\uD83D\uDE44\uD83D\uDE0F\uD83D\uDE1C\uD83E\uDD10\uD83D\uDE14\uD83E\uDD2C\uD83D\uDE21\uD83D\uDE37\uD83D\uDE31\uD83D\uDE2C\uD83D\uDE27\uD83E\uDD24☹\uD83D\uDE33\uD83E\uDD22\uD83E\uDD22\uD83E\uDD27\uD83D\uDE07\uD83D\uDE08\uD83D\uDE08\uD83D\uDC7F\uD83D\uDE40\uD83E\uDD16\uD83D\uDC7D\uD83D\uDC80☠\uD83D\uDC7B\uD83D\uDE3B\uD83D\uDE39\uD83D\uDE38\uD83D\uDE3B\uD83D\uDE4A\uD83D\uDE49\uD83D\uDE48\uD83D\uDC69\uD83E\uDDD2\uD83D\uDC74\uD83D\uDC66\uD83D\uDC68\u200D⚕\uD83D\uDC68\u200D\uD83C\uDF93\uD83D\uDC68\u200D\uD83C\uDF73\uD83D\uDC68\u200D✈\uD83E\uDDD5\uD83D\uDC72\uD83D\uDC73\u200D♀\uD83D\uDC73\uD83E\uDD34\uD83D\uDD75\uD83D\uDC82\uD83D\uDC69\u200D\uD83C\uDFA4\uD83D\uDC68\u200D\uD83C\uDFA4\uD83D\uDC69\u200D\uD83D\uDCBB\uD83D\uDC68\u200D\uD83D\uDCBB\uD83D\uDC86\uD83D\uDE4B\u200D♂\uD83E\uDDD8\uD83E\uDDD7\uD83D\uDC6F\uD83D\uDC86\u200D♂\uD83C\uDFC3\u200D♀\uD83C\uDFC3\u200D♀\uD83D\uDECC⛷\uD83E\uDD38\u200D♂\uD83C\uDFCB\u200D♀\uD83C\uDFC4\uD83D\uDEB5\uD83E\uDD39\uD83E\uDD3E\uD83E\uDD3C\uD83E\uDD39\uD83E\uDD3E\uD83C\uDFC4\uD83D\uDC91\uD83D\uDC97\uD83E\uDD1A\uD83D\uDC85✋✋\uD83D\uDD95\uD83D\uDC46\uD83D\uDC46☝\uD83D\uDC49\uD83D\uDC4A\uD83E\uDD1F\uD83D\uDE4C✍\uD83E\uDD1E\uD83D\uDD96\uD83D\uDC4F\uD83D\uDC43\uD83D\uDC4D\uD83E\uDD19\uD83E\uDD1C\uD83E\uDD1D\uD83D\uDC95\uD83D\uDC9D\uD83D\uDCA6\uD83D\uDCA8\uD83D\uDCA5\uD83D\uDCA3\uD83D\uDCA4\uD83D\uDC8C\uD83E\uDDE3\uD83D\uDC51\uD83D\uDCAD\uD83D\uDECD\uD83C\uDF92\uD83D\uDC60\uD83D\uDC54\uD83D\uDCAC\uD83E\uDD92\uD83D\uDC3F\uD83E\uDD89\uD83D\uDC13\uD83D\uDC13\uD83D\uDC13\uD83D\uDC13\uD83D\uDC33\uD83D\uDC1A\uD83D\uDC1B\uD83D\uDC0C\uD83E\uDD91\uD83D\uDD78\uD83E\uDD82\uD83D\uDD77\uD83D\uDD78\uD83D\uDC90\uD83D\uDCAE\uD83C\uDFF5\uD83E\uDD90\uD83E\uDD95\uD83D\uDC27\uD83D\uDC24\uD83C\uDF54\uD83E\uDD55\uD83E\uDD5D\uD83C\uDF73\uD83C\uDF7F\uD83C\uDF44\uD83C\uDF5E\uD83C\uDF8F\uD83C\uDF8D\uD83C\uDF8D\uD83C\uDF8F\uD83C\uDF8F\uD83C\uDFC9\uD83C\uDFC9\uD83C\uDFC9\uD83C\uDFD2\uD83C\uDFD2\uD83C\uDFD1\uD83C\uDF9F\uD83C\uDF9F\uD83C\uDF9F\uD83C\uDF8E\uD83C\uDF8E\uD83C\uDF91\uD83C\uDFC5\uD83C\uDFC5\uD83C\uDFC9\uD83C\uDFF8\uD83C\uDFF8\uD83C\uDF0F\uD83C\uDF0F\uD83D\uDDFA\uD83D\uDDFA\uD83E\uDD4C\uD83E\uDD4C\uD83C\uDFB4⛺⛲⛲⛩⛩\uD83C\uDFEF\uD83C\uDFEF\uD83C\uDFEF\uD83C\uDFE6♨\uD83D\uDE86\uD83C\uDFAA\uD83D\uDEF8\uD83C\uDF06\uD83D\uDEE9\uD83D\uDECE\uD83D\uDD60\uD83D\uDD5C\uD83D\uDD61\uD83D\uDD65\uD83D\uDD60\uD83D\uDD53\uD83D\uDD52\uD83D\uDEBD\uD83D\uDEBD\uD83D\uDCBA⛴\uD83D\uDECB\uD83C\uDF29\uD83C\uDF1B\uD83C\uDF14\uD83C\uDF15\uD83C\uDF20\uD83C\uDF1E\uD83C\uDF17\uD83C\uDF24\uD83C\uDF08\uD83D\uDD09\uD83D\uDD07\uD83D\uDCFB\uD83C\uDFB9\uD83D\uDCE2\uD83D\uDCEF\uD83C\uDFA4\uD83D\uDD0C\uD83D\uDCDC\uD83D\uDD0E\uD83D\uDDB2\uD83D\uDD2D\uD83D\uDCDA\uD83D\uDCB8\uD83C\uDFF7\uD83D\uDCF0\uD83D\uDD6F\uD83D\uDCD8\uD83D\uDCEE✏\uD83D\uDD8B\uD83D\uDD8A\uD83D\uDCCB✂\uD83D\uDCCF\uD83D\uDCCD⛏⚒\uD83D\uDC8A\uD83D\uDEB9\uD83D\uDEB0\uD83C\uDFE7\uD83D\uDEB3\uD83D\uDEC3⚠\uD83D\uDEB7\uD83D\uDD1E\uD83D\uDEAF\uD83D\uDD02\uD83D\uDD4E♈♒\uD83D\uDD3D\uD83C\uDFA6©❇✖\uD83D\uDCDB\uD83D\uDD06\uD83D\uDCF3\uD83D\uDCF4⃣\uD83C\uDD93\uD83D\uDD20\uD83D\uDCAF\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE39\uD83C\uDE1A\uD83C\uDE51\uD83C\uDE33\uD83C\uDE34\uD83D\uDD38\uD83D\uDD37⬛\uD83C\uDDE6\uD83C\uDDEC\uD83D\uDEA9\uD83C\uDFF3\u200D\uD83C\uDF08\uD83C\uDFC3\u200D♀";

    RaidCommand() {
        super("raid");
        setPermission("roflanboat.raid", "Ты кто такой, чтобы это сделать? Команда не найдена, крч\nВведи пароль: /access <Пароль>");
        setAlias("access");
    }

    private static final String PASSWORD = "boat";

    @Override
    public final void execute(@NotNull User sender, @NotNull String[] args) {
        Chat<?> chat = sender.getChat();
        if(args[0].equalsIgnoreCase("access")) {
            if(!args[1].equalsIgnoreCase(PASSWORD)) {
                chat.sendMessage("Даун, ливни из жизни");
                return;
            }
            chat.sendMessage("Успешно, сын дерьма ебанного");
            PermissionManager.STAFF_ADMIN.put(sender.getUserId(), "NotFound");
        } else {
            if(noPermission(sender)) {
                return;
            }
            if (args.length == 1) {
                if (raids.containsKey(sender.getPeerId())) {
                    chat.sendMessage("Рейд уже запущен");
                    return;
                }
                startRaid(chat, CHELIBOSI, 3);
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("stop")) {
                    if (!raids.containsKey(sender.getPeerId())) {
                        chat.sendMessage("Рейд еще не запущен");
                        return;
                    }
                    RoflanRunnable runnable = raids.remove(sender.getPeerId());
                    runnable.cancel();
                    chat.sendMessage("ZA WARDO");
                }
            } else {
                int period;
                try {
                    period = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    period = 3;
                }
                if (period < 2) {
                    chat.sendMessage("Слишком маленькая задержка, начинай с 2-х");
                    period = 2;
                }
                @NonNls String text = StringBind.toString(2, args) + '\n';
                text = text.repeat(lenghtNotify(text.length()));
                startRaid(chat, text, period);
            }
        }
    }
    private static int lenghtNotify(int lenghtSource) {
        return 2500/lenghtSource;
    }
    private void startRaid(@NotNull Chat<?> chat, String text, int period) {
        if(raids.containsKey(chat.getPeerId())) {
            chat.sendMessage("В этой беседе уже идет рейд!");
            return;
        }
        RaidRunnable raidRunnable = new RaidRunnable(chat, text);
        raidRunnable.runTaskTimer(0, period);
        raids.put(chat.getPeerId(), raidRunnable);
    }

    private static final class RaidRunnable extends RoflanRunnable {
        private final Chat<?> chat;
        private final String message;

        private RaidRunnable(Chat<?> chat, String message) {
            this.chat = chat;
            this.message = message;
        }

        @Override
        public void run() {
            chat.sendMessage(message);
        }
    }
}
