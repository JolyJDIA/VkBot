package jolyjdia.vk.api.objects.messages;

import com.google.gson.annotations.SerializedName;
import jolyjdia.vk.api.objects.Validable;
import jolyjdia.vk.api.objects.annotations.Required;
import jolyjdia.vk.api.objects.base.BoolInt;
import jolyjdia.vk.api.objects.base.Geo;

import java.util.List;

public class Message implements Validable {
    @SerializedName("action")
    private MessageAction action;

    @SerializedName("admin_author_id")
    private int adminAuthorId;

    @SerializedName("attachments")
    private List<MessageAttachment> attachments;

    @SerializedName("conversation_message_id")
    private int conversationMessageId;

    @SerializedName("date")
    @Required
    private int date;

    @SerializedName("deleted")
    private BoolInt deleted;

    @SerializedName("from_id")
    private int fromId;

    @SerializedName("fwd_messages")
    private List<ForeignMessage> fwdMessages;

    @SerializedName("geo")
    private Geo geo;

    @SerializedName("id")
    @Required
    private int id;

    @SerializedName("important")
    private boolean important;

    @SerializedName("is_hidden")
    private boolean isHidden;

    @SerializedName("keyboard")
    private Keyboard keyboard;

    @SerializedName("members_count")
    private int membersCount;

    @SerializedName("out")
    @Required
    private BoolInt out;

    @SerializedName("payload")
    private String payload;

    @SerializedName("peer_id")
    private int peerId;

    @SerializedName("random_id")
    private int randomId;

    @SerializedName("ref")
    private String ref;

    @SerializedName("ref_source")
    private String refSource;

    @SerializedName("reply_message")
    private ForeignMessage replyMessage;

    @SerializedName("text")
    @Required
    private String text;


    @SerializedName("update_time")
    private int updateTime;

    public MessageAction getAction() {
        return action;
    }

    public Message setAction(MessageAction action) {
        this.action = action;
        return this;
    }

    public int getAdminAuthorId() {
        return adminAuthorId;
    }

    public Message setAdminAuthorId(int adminAuthorId) {
        this.adminAuthorId = adminAuthorId;
        return this;
    }

    public List<MessageAttachment> getAttachments() {
        return attachments;
    }

    public Message setAttachments(List<MessageAttachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public int getConversationMessageId() {
        return conversationMessageId;
    }

    public Message setConversationMessageId(int i) {
        this.conversationMessageId = i;
        return this;
    }

    public int getDate() {
        return date;
    }

    public Message setDate(int date) {
        this.date = date;
        return this;
    }

    public boolean isDeleted() {
        return deleted == BoolInt.YES;
    }

    public BoolInt getDeleted() {
        return deleted;
    }

    public int getFromId() {
        return fromId;
    }

    public Message setFromId(int fromId) {
        this.fromId = fromId;
        return this;
    }

    public List<ForeignMessage> getFwdMessages() {
        return fwdMessages;
    }

    public Message setFwdMessages(List<ForeignMessage> fwdMessages) {
        this.fwdMessages = fwdMessages;
        return this;
    }

    public Geo getGeo() {
        return geo;
    }

    public Message setGeo(Geo geo) {
        this.geo = geo;
        return this;
    }

    public int getId() {
        return id;
    }

    public Message setId(int id) {
        this.id = id;
        return this;
    }

    public boolean getImportant() {
        return important;
    }

    public Message setImportant(boolean important) {
        this.important = important;
        return this;
    }

    public boolean getIsHidden() {
        return isHidden;
    }

    public Message setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Message setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
        return this;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public Message setMembersCount(int membersCount) {
        this.membersCount = membersCount;
        return this;
    }

    public boolean isOut() {
        return out == BoolInt.YES;
    }

    public BoolInt getOut() {
        return out;
    }

    public String getPayload() {
        return payload;
    }

    public Message setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public int getPeerId() {
        return peerId;
    }

    public Message setPeerId(int peerId) {
        this.peerId = peerId;
        return this;
    }

    public int getRandomId() {
        return randomId;
    }

    public Message setRandomId(int randomId) {
        this.randomId = randomId;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public Message setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public String getRefSource() {
        return refSource;
    }

    public Message setRefSource(String refSource) {
        this.refSource = refSource;
        return this;
    }

    public ForeignMessage getReplyMessage() {
        return replyMessage;
    }

    public Message setReplyMessage(ForeignMessage replyMessage) {
        this.replyMessage = replyMessage;
        return this;
    }

    public String getText() {
        return text;
    }

    public Message setText(String text) {
        this.text = text;
        return this;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public Message setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
