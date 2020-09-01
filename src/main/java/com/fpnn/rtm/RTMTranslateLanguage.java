package com.fpnn.rtm;

public enum RTMTranslateLanguage {
    AR("ar"),           //阿拉伯语
    NL("nl"),           //荷兰语
    EN("en"),           //英语
    FR("fr"),           //法语
    DE("de"),           //德语
    EL("el"),           //希腊语
    ID("id"),           //印度尼西亚语
    IT("it"),           //意大利语
    JA("ja"),           //日语
    KO("ko"),           //韩语
    NO("no"),           //挪威语
    PL("pl"),           //波兰语
    PT("pt"),           //葡萄牙语
    RU("ru"),           //俄语
    ES("es"),           //西班牙语
    SV("sv"),           //瑞典语
    TL("tl"),           //塔加路语（菲律宾语）
    TH("th"),           //泰语
    TR("tr"),           //土耳其语
    VI("vi"),           //越南语
    ZH_CN("zh_cn"),     //中文（简体）
    ZH_TW("zh_tw");     //中文（繁体）

    private final String value;

    RTMTranslateLanguage(String lang){
        this.value = lang;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
