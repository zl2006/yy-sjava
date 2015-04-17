package org.yy.framework.basedata;

/**
 * 淘宝应用相关常量
 * 
 * @author zhouliang
 * @version [1.0, 2014年5月20日]
 * @since [taobao-base/1.0]
 */
public final class Constants {
    
    private Constants() {
    }
    
    /** 默认时间格式 **/
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /** UTF-8字符集 **/
    public static final String CHARSET_UTF8 = "UTF-8";
    
    /** GBK字符集 **/
    public static final String CHARSET_GBK = "GBK";
    
    /** JSON 应格式 */
    public static final String FORMAT_JSON = "json";
    
    /** XML 应格式 */
    public static final String FORMAT_XML = "xml";
    
    /** MD5签名方式 */
    public static final String SIGN_METHOD_MD5 = "md5";
    
    /** TOKEN */
    public static final String MVC_TOKEN_KEY = "_request_token_";
    
    /*************************请求源配置****************************************/
    /**请求来源*/
    public static final String REQ_FROM_SOURCE = "_src_";
    
    /**PC源*/
    public static final String PC_SOURCE = "pc";
    
    /**手机源*/
    public static final String M_SOURCE = "m";
    
    /*************************end********************************************/
    
    /*************************编码及返回页面***********************************/
    /** 通用访问成功时的标志位 */
    public static final String SUCCESS_FLAG = "success";
    
    /** 通用访问失败时的标志位 */
    public static final String FAILURE_FLAG = "failure";
    
    /**通用错误编码*/
    public static final String ERROR_500_CODE = "500";
    
    /** PC端404错误页 */
    public static final String ERROR_404_PAGE = "/common/error404";
    
    /** PC端500错误页 */
    public static final String ERROR_500_PAGE = "/common/error500";
    
    /** PC端成功页 */
    public static final String SUCCESS_PAGE = "/common/success";
    
    /**增加页面*/
    public static final String ADD_PAGE = "add";
    
    /**编辑页面*/
    public static final String EDIT_PAGE = "edit";
    
    /**删除页面*/
    public static final String DELETE_PAGE = "delete";
    
    /**列表页*/
    public static final String LIST_PAGE = "list";
    
    /**审核页*/
    public static final String REVIEW_PAGE = "review";
    
    /**详情页*/
    public static final String VIEW_PAGE = "view";
    
    /** 手机端404错误页 */
    public static final String M_ERROR_404_PAGE = "/common/merror404";
    
    /** 手机端500错误页 */
    public static final String M_ERROR_500_PAGE = "/common/merror500";
    
    /** 手机端成功页 */
    public static final String M_SUCCESS_PAGE = "/common/msuccess";
    
    /*************************end************************************************/
    
    /*************************控制层返回的数据结果关键字**************************************/
    /** 结果集标志的名称 */
    public static final String RESULT_FLAG_NAME = "flag";
    
    /** 结果集编码的名称 */
    public static final String RESULT_CODE_NAME = "code";
    
    /** 结果集message值的名称 */
    public static final String RESULT_MESSAGE_NAME = "msg";
    
    /** 结果集data值的名称 */
    public static final String RESULT_DATA_NAME = "data";
    
    /** 结果集请求参数值的名称 */
    public static final String RESULT_PARAM_NAME = "params";
    
    /** 结果集errors值的名称 */
    public static final String RESULT_ERRORS_NAME = "errors";
    
    /** 结果集的名称 */
    public static final String RESULT_NAMES[] = {RESULT_FLAG_NAME, RESULT_CODE_NAME, RESULT_MESSAGE_NAME,
        RESULT_DATA_NAME, RESULT_ERRORS_NAME};
    /*******************************************end**************************************/
    
}
