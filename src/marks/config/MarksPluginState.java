package marks.config;

import marks.pairing.TeamMember;

import java.util.ArrayList;
import java.util.List;

public class MarksPluginState {
    private Integer mRegionRGB;
//    private List<TeamMember> mPairs = new ArrayList<TeamMember>();
//
//    public List<TeamMember> getPairs() {
//        return mPairs;
//    }
//
//    public void setPairs(List<TeamMember> pairs) {
//        mPairs = pairs;
//    }

    public Integer getRegionRGB() {
        return mRegionRGB;
    }

    public void setRegionRGB(Integer regionRGB) {
        mRegionRGB = regionRGB;
    }
}
