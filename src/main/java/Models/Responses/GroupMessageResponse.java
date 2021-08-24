package Models.Responses;

import java.util.ArrayList;

public class GroupMessageResponse {
    private ArrayList<String> followings, groups, conversations;
    private ArrayList<Integer> groupsId;

    public ArrayList<Integer> getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(ArrayList<Integer> groupsId) {
        this.groupsId = groupsId;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public ArrayList<String> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<String> conversations) {
        this.conversations = conversations;
    }
}
