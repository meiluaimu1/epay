package org.epay.model.base;

import java.io.Serializable;
import java.util.Date;

public class App implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_id
     *
     * @mbg.generated
     */
    private String appId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_name
     *
     * @mbg.generated
     */
    private String appName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_key
     *
     * @mbg.generated
     */
    private String appKey;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_create_time
     *
     * @mbg.generated
     */
    private Date appCreateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_update_time
     *
     * @mbg.generated
     */
    private Date appUpdateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_state
     *
     * @mbg.generated
     */
    private Byte appState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_return_url
     *
     * @mbg.generated
     */
    private String appReturnUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column app.app_notify_url
     *
     * @mbg.generated
     */
    private String appNotifyUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table app
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_id
     *
     * @return the value of app.app_id
     *
     * @mbg.generated
     */
    public String getAppId() {
        return appId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_id
     *
     * @param appId the value for app.app_id
     *
     * @mbg.generated
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_name
     *
     * @return the value of app.app_name
     *
     * @mbg.generated
     */
    public String getAppName() {
        return appName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_name
     *
     * @param appName the value for app.app_name
     *
     * @mbg.generated
     */
    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_key
     *
     * @return the value of app.app_key
     *
     * @mbg.generated
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_key
     *
     * @param appKey the value for app.app_key
     *
     * @mbg.generated
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_create_time
     *
     * @return the value of app.app_create_time
     *
     * @mbg.generated
     */
    public Date getAppCreateTime() {
        return appCreateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_create_time
     *
     * @param appCreateTime the value for app.app_create_time
     *
     * @mbg.generated
     */
    public void setAppCreateTime(Date appCreateTime) {
        this.appCreateTime = appCreateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_update_time
     *
     * @return the value of app.app_update_time
     *
     * @mbg.generated
     */
    public Date getAppUpdateTime() {
        return appUpdateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_update_time
     *
     * @param appUpdateTime the value for app.app_update_time
     *
     * @mbg.generated
     */
    public void setAppUpdateTime(Date appUpdateTime) {
        this.appUpdateTime = appUpdateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_state
     *
     * @return the value of app.app_state
     *
     * @mbg.generated
     */
    public Byte getAppState() {
        return appState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_state
     *
     * @param appState the value for app.app_state
     *
     * @mbg.generated
     */
    public void setAppState(Byte appState) {
        this.appState = appState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_return_url
     *
     * @return the value of app.app_return_url
     *
     * @mbg.generated
     */
    public String getAppReturnUrl() {
        return appReturnUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_return_url
     *
     * @param appReturnUrl the value for app.app_return_url
     *
     * @mbg.generated
     */
    public void setAppReturnUrl(String appReturnUrl) {
        this.appReturnUrl = appReturnUrl == null ? null : appReturnUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column app.app_notify_url
     *
     * @return the value of app.app_notify_url
     *
     * @mbg.generated
     */
    public String getAppNotifyUrl() {
        return appNotifyUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column app.app_notify_url
     *
     * @param appNotifyUrl the value for app.app_notify_url
     *
     * @mbg.generated
     */
    public void setAppNotifyUrl(String appNotifyUrl) {
        this.appNotifyUrl = appNotifyUrl == null ? null : appNotifyUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        App other = (App) that;
        return (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
            && (this.getAppName() == null ? other.getAppName() == null : this.getAppName().equals(other.getAppName()))
            && (this.getAppKey() == null ? other.getAppKey() == null : this.getAppKey().equals(other.getAppKey()))
            && (this.getAppCreateTime() == null ? other.getAppCreateTime() == null : this.getAppCreateTime().equals(other.getAppCreateTime()))
            && (this.getAppUpdateTime() == null ? other.getAppUpdateTime() == null : this.getAppUpdateTime().equals(other.getAppUpdateTime()))
            && (this.getAppState() == null ? other.getAppState() == null : this.getAppState().equals(other.getAppState()))
            && (this.getAppReturnUrl() == null ? other.getAppReturnUrl() == null : this.getAppReturnUrl().equals(other.getAppReturnUrl()))
            && (this.getAppNotifyUrl() == null ? other.getAppNotifyUrl() == null : this.getAppNotifyUrl().equals(other.getAppNotifyUrl()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getAppName() == null) ? 0 : getAppName().hashCode());
        result = prime * result + ((getAppKey() == null) ? 0 : getAppKey().hashCode());
        result = prime * result + ((getAppCreateTime() == null) ? 0 : getAppCreateTime().hashCode());
        result = prime * result + ((getAppUpdateTime() == null) ? 0 : getAppUpdateTime().hashCode());
        result = prime * result + ((getAppState() == null) ? 0 : getAppState().hashCode());
        result = prime * result + ((getAppReturnUrl() == null) ? 0 : getAppReturnUrl().hashCode());
        result = prime * result + ((getAppNotifyUrl() == null) ? 0 : getAppNotifyUrl().hashCode());
        return result;
    }
}