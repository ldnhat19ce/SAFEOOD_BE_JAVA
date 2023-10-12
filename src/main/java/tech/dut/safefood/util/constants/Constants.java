package tech.dut.safefood.util.constants;


public class Constants {

    public static final String JWT_CLAIM_KEY_IS_ADMIN_API = "isAdminApi";

    public static final String JWT_CLAIM_KEY_IS_EMAIL_API = "email";
    public static final String API_ADMIN_URL_REGEX = "/api/admin";
    public static final Integer DIGIT_CODE_LENGTH = 6;

    public static final String ACTIVE_ACCOUNT_BY_CODE = "Active account by code";

    public static final String ACTIVE_SHOP = "Active shop";

    public static final String EMAIL_SEND_FROM_DA_PASS_NAME = "Safe Food";

    public static final String TEMPLATE_FORGOT_PASSWORD_FORM = "forgot_password_form";

    public static final String TEMPLATE_ACTIVE_CODE = "active_code";

    public static final String TEMPLATE_ACTIVE_SHOP = "active_shop";

    public static final String RESET_YOUR_PASSWORD = "Reset your password";

    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    public static final String ROLE_USER = "user";

    public static final String ROLE_ADMIN = "admin";

    public static final String ROLE_SHOP = "shop";
    public static final String GOOGLE_INFO_URI = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    public static final Object FACEBOOK_INFO_URI = "https://graph.facebook.com/v14.0/me?fields=id,first_name,middle_name,last_name,name,email,picture";

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DAY_AT_ZERO_O_CLOCK = "00:00:00";
    public static final String DAY_AFTER_AT_ZERO_O_CLOCK = "23:59:59";
    public static final String VNP_DATE_FORMAT = "yyyyMMddHHmmss";
    public static final CharSequence PLUS = "+";

    public static final String BILL_PENDING = "PENDING";

    public static final String BILL_PAYMENTED = "PAYMENTED";

    public static final String BILL_DONE = "DONE";

    public static final String BILL_FAIL = "FAILED";

    public static final String BILL_CREATED = "CREATED";

    public static final String PAYMENT_SUCCESS = "SUCCESS";

    public static final String PAYMENT_FAIL = "FAIL";

    public static final String VNPAY = "VNPAY";

    public static final String PRICE = "PRICE";

    public static final String VOUCHER_TYPE_PERCENT = "PERCENT";

    public static final String PAYMENT_PENDING = "PENDING_PRICE";

    public static final String VOUCHER_TYPE_PRICE = "PRICE";

    public static final String AWS_PRODUCT_IMAGE_FOLDER = "photo/product/";

    public static final String AWS_SHOP_IMAGE_FOLDER = "photo/shop/";
    public static final String AWS_VOUCHER_IMAGE_FOLDER = "photo/voucher/";
    public static final String AWS_USER_IMAGE_FOLDER = "photo/user/";
    public static final String AWS_CATEGORY_IMAGE_FOLDER = "photo/category/";
    public static final String AWS_BILL_IMAGE_FOLDER = "photo/bill/";
    public static final String AWS_NEWS_IMAGE_FOLDER = "photo/news/";

    public static final String SHOP_SCHEDULE_CLOSE = "CLOSE";


    public static final String SHOP_SCHEDULE_OPEN = "OPEN";
    public static final String PRODUCT_STATUS_NEW = "NEW";

    public static final int EARTH_RADIUS = 6371;

    public static final String PRODUCT_STATUS_RECOMMEND = "RECOMMEND";

    public static final String PRODUCT_STATUS_DELETED = "DELETED";

    public static final String VOUCHER_STATUS_ACTIVED = "ACTIVED";

    public static final String VOUCHER_STATUS_DELETED = "DELETED";

    public static final String VOUCHER_STATUS_INACTIVE = "INACTIVE";

    public static final String SORT_OLDEST_TO_NEWEST = "SORT_OLDEST_TO_NEWEST";
    public static final String SORT_NEWEST_TO_OLDEST = "SORT_NEWEST_TO_OLDEST";
    public static final String WEB_VIEW_NEWS = "<!DOCTYPE html><html lang=en><head><meta charset=UTF-8><meta http-equiv=X-UA-Compatible content=IE=edge> <meta name=viewport content=width=device-width, initial-scale=1.0> <title>Dapass Detail News</title> <style> * {font-family: 'NanumBarunGothic';} body {margin: 0;padding: 0;} img {width: 100%%; height: auto;} .header {position: relative;} .header_content {position: absolute;width: 100%%;top: 0px;padding-top: 80px;color: #FFFFFF;background: linear-gradient(180deg, rgba(0, 0, 0, 0.45) 31.77%%, rgba(0, 0, 0, 0) 100%%);} .no_wrap {line-height: 1.6rem;overflow: hidden;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;max-height: 3.2rem;}  .title {font-size: 20px;font-weight: 600;margin: 10px 15px;} .sub_title {margin: 10px 15px;font-size: 16px;font-weight: 400;} .content {padding: 20px 16px;} .content img {padding: 10px 0px;} </style> </head> <body> <div class=header><img src=%s alt=> <div class=header_content> <h4 class=title no_wrap> %s </h4> <p class=sub_title no_wrap> %s </p> </div></div> <div class=content> %s </div> </body></html>";

    /**
     * Notification Review
     */
    public static final String NOTIFICATION_COMMUNITY_TWO_USER = "%s and %s";
    public static final String NOTIFICATION_COMMUNITY_MULTIPLE_USER = "%s and %s Others";
    public static final String NOTIFICATION_COMMUNITY_USER_REACTED_REVIEW_CONTENT = "Liked the post";
    public static final String NOTIFICATION_COMMUNITY_USER_COMMENT_REVIEW_CONTENT = "Commented the post";
    public static final String NOTIFICATION_COMMUNITY_USER_REACTED_COMMENT_CONTENT = "Liked the comment";
    public static final String NOTIFICATION_COMMUNITY_USER_COMMENT_REPLY_CONTENT = "Commented on reply";
    public static final String AWS_COMMUNITY_IMAGE_FOLDER = "photo/community/";
}
