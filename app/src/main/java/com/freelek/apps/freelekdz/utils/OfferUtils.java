package com.freelek.apps.freelekdz.utils;

import com.freelek.apps.freelekdz.classes.Currency;
import com.freelek.apps.freelekdz.classes.Setting;
import com.freelek.apps.freelekdz.controllers.SettingsController;
import com.freelek.apps.freelekdz.parser.api_parser.OfferCurrencyParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by Droideve on 1/26/2018.
 */

public class OfferUtils {


    public static String parseCurrencyFormat(double price, Currency cData) {

        DecimalFormat decim = new DecimalFormat("0.00");

        String ps = decim.format(price);

        if (cData != null) {
            switch (cData.getFormat()) {
                case 1:
                    return cData.getSymbol() + ps;
                case 2:
                    return ps + cData.getSymbol();
                case 3:
                    return cData.getSymbol() + " " + ps;
                case 4:
                    return ps + " " + cData.getSymbol();
                case 5:
                    return String.valueOf(ps);
                case 6:
                    return cData.getSymbol() + ps + " " + cData.getCode();
                case 7:
                    return cData.getSymbol() + ps;
                case 8:
                    return ps + cData.getCode();
            }

        }

        return String.valueOf(price);


    }


    public static Currency defaultCurrency() {
        //get currrency from appConfig API
        String defaultLocalCurrency = null;
        Setting defaultAppSetting = SettingsController.findSettingFiled("CURRENCY_OBJECT");
        if (defaultAppSetting != null && !defaultAppSetting.getValue().equals("")) {
            defaultLocalCurrency = defaultAppSetting.getValue();

            OfferCurrencyParser mProductCurrencyParser = null;
            try {
                mProductCurrencyParser = new OfferCurrencyParser(new JSONObject(
                        defaultLocalCurrency
                ));

                return mProductCurrencyParser.getCurrency();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return null;
    }
}
