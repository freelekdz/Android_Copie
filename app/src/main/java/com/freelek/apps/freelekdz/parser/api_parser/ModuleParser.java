package com.freelek.apps.freelekdz.parser.api_parser;



import com.freelek.apps.freelekdz.appconfig.AppContext;
import com.freelek.apps.freelekdz.classes.Module;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;


public class ModuleParser extends Parser {

    public ModuleParser(JSONObject json) {
        super(json);
    }

    public RealmList<Module> getModules() {

        RealmList<Module> list = new RealmList<Module>();
        try {

            JSONObject json_array = json.getJSONObject(Tags.RESULT);

            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_modules = json_array.getJSONObject(i + "");
                Module mModule = new Module();

                mModule.setEnabled(json_modules.getInt("enabled"));
                mModule.setName(json_modules.getString("module_name"));

                list.add(mModule);

            }

        } catch (JSONException e) {
            if (AppContext.DEBUG)
                e.printStackTrace();
        }


        return list;
    }


}
