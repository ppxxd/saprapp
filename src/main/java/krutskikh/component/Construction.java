package krutskikh.component;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Construction implements Serializable {
    private List<Bar> bars = new ArrayList<>();
    private List<Joint> joints = new ArrayList<>();
    private Boolean leftSupport = true;
    private Boolean rightSupport = false;

    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }

    public void setJoints(List<Joint> joints) {
        this.joints = joints;
    }

    public void setLeftSupport(Boolean leftSupport) {
        this.leftSupport = leftSupport;
    }

    public void setRightSupport(Boolean rightSupport) {
        this.rightSupport = rightSupport;
    }
}
