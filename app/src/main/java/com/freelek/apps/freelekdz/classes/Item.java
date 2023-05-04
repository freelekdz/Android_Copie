package com.freelek.apps.freelekdz.classes;

import com.mikepenz.iconics.typeface.IIcon;


public class Item {

    public final static String TAG_NAME = "item";
    protected String type;
    private boolean enabled = true;
    private int ID;
    private String Name;
    private String Discription;
    private String ImageUrl;
    private int ImageId = 0;
    private int notify;
    private IIcon iconDraw;

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public Item() {
        this.type = TAG_NAME;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public IIcon getIconDraw() {
        return iconDraw;
    }

    public void setIconDraw(IIcon iconDraw) {
        this.iconDraw = iconDraw;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
