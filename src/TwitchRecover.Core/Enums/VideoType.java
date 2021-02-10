package TwitchRecover.Core.Enums;

/**
 * Enum which represents all of the officially
 * supported broadcast types.
 */
public enum VideoType {
    ARCHIVE,
    HIGHLIGHT,
    UPLOAD,
    PREMIERE_UPLOAD,
    PAST_PREMIERE;

    VideoType(){
    }

    /**
     * This method takes in a string input
     * and returns the corresponding video
     * type enum.
     * @param vt    String variable representing the video type to search.
     * @return VideoType    VideoType enum which represents the VideoType enum that is equal to the given string value.
     */
    public static VideoType getVideoType(String vt){
        for(VideoType videoType: VideoType.values()){
            if(videoType.toString().equals(vt)){
                return videoType;
            }
        }
        return null;
    }
}
