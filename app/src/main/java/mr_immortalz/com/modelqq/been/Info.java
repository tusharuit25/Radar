package mr_immortalz.com.modelqq.been;
/**
 * Created by DELL2 on 7/27/2016.
 */
public class Info {
    private int itemid;
    private int portraitId;//头像id
    private String name;//名字
    private int age;//年龄
    private boolean meetup0_user1;//false为男，true为女
    private double distance;//距离
    private  String mutualsport;
    String GallaryImage;
    String FollowStatus;
    String Location;
    long LikeCount;
    String Meetuptime;
    String UserId;
    String MeetupId;

    public String getMeetupId() {
        return MeetupId;
    }

    public void setMeetupId(String meetupId) {
        MeetupId = meetupId;
    }

    String ClientUsername;
    private String ProfilePic;
    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public long getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(long likeCount) {
        LikeCount = likeCount;
    }



    public String getMeetuptime() {
        return Meetuptime;
    }

    public void setMeetuptime(String meetuptime) {
        Meetuptime = meetuptime;
    }



    public String getClientUsername() {
        return ClientUsername;
    }

    public void setClientUsername(String clientUsername) {
        ClientUsername = clientUsername;
    }



    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getLocation() {

        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getFollowStatus() {
        return FollowStatus;
    }

    public void setFollowStatus(String followStatus) {
        FollowStatus = followStatus;
    }

    public String getGallaryImage() {
        return GallaryImage;
    }

    public void setGallaryImage(String gallaryImage) {
        GallaryImage = gallaryImage;
    }

    public String getMutualsport() {
        return mutualsport;
    }

    public void setMutualsport(String mutualsport) {
        this.mutualsport = mutualsport;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public boolean isMeetup0_user1() {
        return meetup0_user1;
    }

    public void setMeetup0_user1(boolean meetup0_user1) {
        this.meetup0_user1 = meetup0_user1;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;

    }



    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
