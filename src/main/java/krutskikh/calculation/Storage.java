package krutskikh.calculation;

import krutskikh.component.Construction;

public enum Storage {
    INSTANCE;
    private Construction constructor;


    public Construction getConstruction() {
        return constructor;
    }

    public void setConstruction(Construction constructor) {
        this.constructor = constructor;
    }
}
