package it.lunacia.yt.constants;

public enum YoutubePlaylistPrivacyEnum {
    PRIVATE("private"),UNLISTED("unlisted"),PUBLIC("public");
    private String status;

    YoutubePlaylistPrivacyEnum(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
