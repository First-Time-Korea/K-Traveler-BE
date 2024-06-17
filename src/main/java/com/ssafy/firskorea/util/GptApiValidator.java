package com.ssafy.firskorea.util;

public class GptApiValidator {

    //TODO: 같은 media 에 대해서는 같은 번역 하도록 묶기

    private GptApiValidator() {
    }

    public static boolean isValidTitle(String englishTitle) {
        String lowerCaseTitle = englishTitle.toLowerCase(); // 소문자 변환
        return lowerCaseTitle.matches(".*[a-z].*") // 대소문자 구분 없이 영어 알파벳 체크
                && !lowerCaseTitle.contains("place")
                && !lowerCaseTitle.contains("attr")
                && !lowerCaseTitle.contains("translated");
    }

    public static boolean isValidAddr(String englishAddr) {
        String lowerCaseAddr = englishAddr.toLowerCase(); // 소문자 변환
        return lowerCaseAddr.matches(".*[a-z].*") // 대소문자 구분 없이 영어 알파벳 체크
                && !lowerCaseAddr.contains("address")
                && !lowerCaseAddr.contains("translated");
    }

    public static boolean isValidOverView(String englishOverView) {
        String lowerCaseOverview = englishOverView.toLowerCase(); // 소문자 변환
        return lowerCaseOverview.matches(".*[a-z].*") // 대소문자 구분 없이 영어 알파벳 체크
                && !lowerCaseOverview.contains("media")
                && !lowerCaseOverview.contains("translated");
    }

    public static boolean isValidLength(String[] englishItem) {
        return englishItem.length == 4;
    }
}
