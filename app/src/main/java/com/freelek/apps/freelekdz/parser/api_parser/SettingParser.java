package com.freelek.apps.freelekdz.parser.api_parser;

import com.freelek.apps.freelekdz.appconfig.AppContext;
import com.freelek.apps.freelekdz.classes.Setting;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;


public class SettingParser extends Parser {

    public SettingParser(JSONObject json) {
        super(json);
    }

    public RealmList<Setting> getSettings() {

        RealmList<Setting> list = new RealmList<Setting>();
        try {

            JSONObject json_array = json.getJSONObject(Tags.RESULT);

            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_settings = json_array.getJSONObject(i + "");
                Setting mSetting = new Setting();
                mSetting.setId(json_settings.getInt("id"));

                if (json_settings.has("_key"))
                    mSetting.set_key(json_settings.getString("_key"));

                if (json_settings.has("value"))
                    mSetting.setValue(json_settings.getString("value"));

                if (json_settings.has("_type"))
                    mSetting.set_type(json_settings.getString("_type"));

                if (json_settings.has("is_verified"))
                    mSetting.setIs_verified(json_settings.getInt("is_verified"));

                if (json_settings.has("user_Id"))
                    mSetting.setUser_id(json_settings.getInt("user_id"));


                if (json_settings.has("version"))
                    mSetting.setVersion(json_settings.getString("version"));

                if (json_settings.has("created_at"))

                    mSetting.setCreated_at(json_settings.getString("created_at"));

                if (json_settings.has("updated_at"))
                    mSetting.setUpdated_at(json_settings.getString("updated_at"));

                list.add(mSetting);

            }

        } catch (JSONException e) {
            if (AppContext.DEBUG)
                e.printStackTrace();
        }


        return list;
    }


}
