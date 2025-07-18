import { NativeModules, NativeEventEmitter } from "react-native";
import {
  UpshotActivityType,
  UpshotGender,
  UpshotMaritalStatus,
  UpshotRewardHistory,
  UpshotInitOptions,
  UpshotAttribution,
  UpshotInboxConfigOptions,
} from "./UpshotConstants";

const UpshotReact = NativeModules.UpshotReact;
var upshotEmitter = new NativeEventEmitter(UpshotReact);

var Upshot = {
  /**
   * initialize Upshot using options
   * Authentication status will be notified through event listener
   * @param {string} options - initialize options
   */
  initializeUpshotUsingOptions: function (options) {
    UpshotReact.initializeUpshotUsingOptions(options);
  },

  /**
   * terminate Upshot
   *
   */
  terminate: function () {
    UpshotReact.terminate();
  },

  /**
   * Set Dispatch Interval / Frequency
   *
   * @param {number} interval - Min Value 10sec and Max 120sec
   *
   */
  setDispatchInterval: function (interval) {
    UpshotReact.setDispatchInterval(interval);
  },

  /**
   * Create PageView / ScreenView event
   *
   * @param {string} ScreenName - the current screen name
   * @param {function(string)} callback- on success of event creation will get eventId else null as a callback
   */
  createPageViewEvent: function (screenName, callback) {
    UpshotReact.createPageViewEvent(screenName, callback);
  },

  /**
   * Create custom event
   *
   * @param {string} eventName - custom event name
   * @param {string} payload - the payload should be json string
   * @param {Boolean} isTimed - isTimed true for Timed events
   * @param {function(string)} callback - on success of event creation will get eventId else null as a callback
   */
  createCustomEvent: function (eventName, payload, isTimed, callback) {
    UpshotReact.createCustomEvent(eventName, payload, isTimed, callback);
  },

  /**
   * Set data and close custom event for eventId
   *
   * @param {string} payload - the payload should be json string
   * @param {string} eventId - eventId to close event
   */

  setValueAndClose: function (payload, eventId) {
    UpshotReact.setValueAndClose(payload, eventId);
  },

  /**
   * close custom event for eventId
   *
   * @param {string} eventId - eventId to close event
   */

  closeEventForId: function (eventId) {
    UpshotReact.closeEventForId(eventId);
  },

  /**
   *  Dispatch all events to server
   *
   * @param {Boolean} timed - timed is true will send all events to server including timed events
   * @param {function(Boolean)} callback - get the status of dispatch events
   */

  dispatchEventsWithTimedEvents: function (timed, callback) {
    UpshotReact.dispatchEventsWithTimedEvents(timed, callback);
  },

  /**
   *  Create location event
   *
   * @param {string} latitude - latitude
   * @param {string} longitude - longitude
   */
  createLocationEvent: function (latitude, longitude) {
    UpshotReact.createLocationEvent(latitude, longitude);
  },

  /**
   *  Create attribution event
   *
   * @param {string} payload - the payload should be json string
   * @param {function(string)} callback - on success of event creation will get eventId else null as a callback
   */
  createAttributionEvent: function (payload, callback) {
    UpshotReact.createAttributionEvent(payload, callback);
  },

  /**
   *  Send user details to upshot
   *
   * @param {string} userData - userData should be json string
   * @param {function(Boolean)} callback - get the status of profile updation
   */
  setUserProfile: function (userData, callback) {
    UpshotReact.setUserProfile(userData, callback);
  },

  /**
   *  get list of user details from Upshot
   *
   * @param {function(response)} callback - get json string as a callback with list of user details
   */
  getUserDetails: function (callback) {
    UpshotReact.getUserDetails(callback);
  },

  /**
   *  Show interactive Tutorial
   *
   * @param {string} tag - requested tag
   */
  showInteractiveTutorial: function (tag) {
    UpshotReact.showInteractiveTutorial(tag);
  },

  /**
   *  Show Activity with type and tag
   *
   * @param {UpshotActivityType} activityType - enum value get it from UpshotConstants
   * @param {string} tag - requested tag
   */
  showActivityWithType: function (type, tag) {
    UpshotReact.showActivityWithType(type, tag);
  },

  /**
   *  Show Activity with activityId
   *
   * @param {string} activityId - activityId given by Upshot
   */

  showActivityWithId: function (activityId) {
    UpshotReact.showActivityWithId(activityId);
  },

  /**
   *  Remove upshot tutorials
   *
   */
  removeTutorials: function () {
    UpshotReact.removeTutorials();
  },

  /**
   *  get list of campaign data from Upshot
   *
   * @param {function(string)} callback - get json string as a callback with list of campaign details
   */

  fetchInboxInfo: function (callback) {
    UpshotReact.fetchInboxInfo(callback);
  },

  /**
   *  get list of user badges from Upshot
   *
   * @param {function(string)} callback - get json string as a callback with list of active and Inactive badges
   */
  getUserBadges: function (callback) {
    UpshotReact.getUserBadges(callback);
  },

  /** Push Notification Module */

  /**
   *  register for push
   *
   */
  registerForPush: function () {
    UpshotReact.registerForPush();
  },

  /**
   *  Send device token to Upshot
   *
   * @param {string} token - device token
   */
  sendDeviceToken: function (token) {
    UpshotReact.sendDeviceToken(token);
  },

  /**
   *  Send push click payload to Upshot
   *
   * @param {string} pushPayload - push click payload
   */
  sendPushDataToUpshot: function (pushPayload) {
    UpshotReact.sendPushDataToUpshot(pushPayload);
  },

  /**
   *  Send Push bundle data to present push notification via Upshot. Android Specific
   *
   * @param {string} pushPayload - push bundle
   */
  displayNotification: function (pushPayload) {
    UpshotReact.displayNotification(pushPayload);
  },

  /**
   *  Disable / delete user as per GDPR
   *
   * @param {boolean} shouldDisable - shouldDisable true for delete account and false for enable
   * @param {function(Boolean)} callback - get the status of diable account status
   */
  disableUser: function (callback) {
    UpshotReact.disableUser(callback);
  },

  /**
   *  Get upshot userId
   *
   * @param {function(string)} callback - get userId generated by Upshot
   */
  getUserId: function (callback) {
    UpshotReact.getUserId(callback);
  },

  /**
   *  Get Upshot SDk version
   *
   * @param {function(string)} callback - get Upshot sdk version
   */
  getSDKVersion: function (callback) {
    UpshotReact.getSDKVersion(callback);
  },

  /**
   *  Get list of active reward program
   *
   * @param {function(string)} successCallback - response is in json string
   * @param {function(error)} failureCallback - will get error in case of any failure
   */
  getRewardsList: function (successCallback, failureCallback) {
    UpshotReact.getRewardsList(successCallback, failureCallback);
  },

  /**
   *  Get history of reward for a given programId
   * @param {string} programId - reward programId
   * @param {Int} historyType - reward history type
   * @param {function(string)} successCallback - response is in json string
   * @param {function(string)} failureCallback - will get error in case of any failure
   */
  getRewardHistoryForProgram: function (
    programId,
    historyType,
    successCallback,
    failureCallback
  ) {
    UpshotReact.getRewardHistoryForProgram(
      programId,
      historyType,
      successCallback,
      failureCallback
    );
  },

  /**
   *  Get history of reward for a given programId
   * @param {string} programId - reward programId
   * @param {function(string)} successCallback - response is in json string
   * @param {function(string)} failureCallback - will get error in case of any failure
   */
  getRewardRulesforProgram: function (
    programId,
    successCallback,
    failurecallback
  ) {
    UpshotReact.getRewardRulesforProgram(
      programId,
      successCallback,
      failurecallback
    );
  },

  /**
   * Redeem rewards for a given programId with redeem amount
   * @param {string} programId - reward programId
   * @param {Int} transactionValue - transactionValue
   * @param {Int} redeemValue - redeem amount
   * @param {string} tag - tag to redeem
   * @param {function(string)} successCallback - response is in json string
   * @param {function(string)} failureCallback - will get error in case of any failure
   */
  redeemRewardsForProgram: function (
    programId,
    transactionValue,
    redeemValue,
    tag,
    successCallback,
    failurecallback
  ) {
    UpshotReact.redeemRewardsForProgram(
      programId,
      transactionValue,
      redeemValue,
      tag,
      successCallback,
      failurecallback
    );
  },

  getPushClickPayload: function (callback) {
    UpshotReact.getPushClickPayload(callback);
  },

  /**
   * Fetch PushNotifications List
   * @param {number} limit - Fetch number of notifications based on limit
   * @param {Boolean} loadMore - Enable load more notifications
   * @param {function(string)} successCallback - response is in json string
   * @param {function(string)} failureCallback - will get error in case of any failure
   */
  getNotificationList: function (
    limit,
    loadMore,
    successCallback,
    failurecallback
  ) {
    UpshotReact.getNotificationList(
      limit,
      loadMore,
      successCallback,
      failurecallback
    );
  },

  /**
   * Fetch Unread Notification Count
   * @param {number} InboxType - Only PushNotifications / Only InApp / Both the notifications
   * @param {function(number)} callback - Will get Unread notification count.
   */
  getUnreadNotificationsCount: function (inboxType, callback) {
    UpshotReact.getUnreadNotificationsCount(inboxType, callback);
  },

  /**
   * Update Notification Read Status
   * @param {string} notificationId - Notification Id
   * @param {function(response)} callback - Will get Unread notification count.
   */
  updateNotificationReadStatus: function (notificationId, callback) {
    UpshotReact.updateNotificationReadStatus(notificationId, callback);
  },

  /**
   * Show Notifications Screen
   * @param {string} Options - InboxScreen Configuration options
   */
  showInboxNotificationScreen: function (options) {
    UpshotReact.showInboxNotificationScreen(options);
  },

  /**
   * Fetch Streaks Data
   * @param {function(string)} successCallback - response is in json string
   * @param {function(string)} failureCallback - will get error in case of any failure
   */
  getStreaksData: function (successCallback, failurecallback) {
    UpshotReact.getStreaksData(successCallback, failurecallback);
  },

  /**
   * Set Font Styles
   * @param {Object} fontStyles - Font styles to be applied
   */
  setFontStyles: function (fontStyles) {
    UpshotReact.setFontStyles(fontStyles);
  },
  /**
   * Add Upshot event listener
   * supported events UpshotAuthStatus, UpshotActivityDidAppear, UpshotActivityDidDismiss, UpshotDeepLink, UpshotPushToken, UpshotPushPayload
   * @param {string} eventName - the Upshot event name
   * @param {function(event)} your event handler
   */
  addListener: function (eventName, handler) {
    if (upshotEmitter) {
      upshotEmitter.addListener(eventName, handler);
    }
  },

  removeEventListener: function (eventName) {
    if (upshotEmitter) {
      upshotEmitter.removeAllListeners(eventName);
    }
  },
};

export {
  UpshotActivityType,
  UpshotGender,
  UpshotMaritalStatus,
  UpshotRewardHistory,
  UpshotInitOptions,
  UpshotAttribution,
  UpshotInboxConfigOptions,
};

export default Upshot;
