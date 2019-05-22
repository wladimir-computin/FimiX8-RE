package com.fimi.libperson;

import com.fimi.libperson.ui.setting.PersonSettingNewActivity;
import com.fimi.libperson.ui.setting.ServiceSettingActivity;
import router.Router;

public class PersonRouter {
    public static void register() {
        Router.router("activity://person.setting", PersonSettingNewActivity.class);
        Router.router("activity://person.service", ServiceSettingActivity.class);
    }
}
