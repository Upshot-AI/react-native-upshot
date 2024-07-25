export default Upshot;
import { UpshotActivityType } from "./UpshotConstants";
import { UpshotGender } from "./UpshotConstants";
import { UpshotMaritalStatus } from "./UpshotConstants";
import { UpshotRewardHistory } from "./UpshotConstants";
import { UpshotInitOptions } from "./UpshotConstants";
import { UpshotAttribution } from "./UpshotConstants";
import { UpshotInboxConfigOptions } from "./UpshotConstants";
declare namespace Upshot {
  function initializeUpshotUsingOptions(options: string): void;
  function terminate(): void;
  function setDispatchInterval(interval: number): void;
  function createPageViewEvent(screenName: any, callback: any): void;
  function createCustomEvent(
    eventName: string,
    payload: string,
    isTimed: boolean,
    callback: (arg0: string) => any
  ): void;
  function setValueAndClose(payload: string, eventId: string): void;
  function closeEventForId(eventId: string): void;
  function dispatchEventsWithTimedEvents(
    timed: boolean,
    callback: (arg0: boolean) => any
  ): void;
  function createLocationEvent(latitude: string, longitude: string): void;
  function createAttributionEvent(
    payload: string,
    callback: (arg0: string) => any
  ): void;
  function setUserProfile(
    userData: string,
    callback: (arg0: boolean) => any
  ): void;
  function getUserDetails(callback: (arg0: response) => any): void;
  function showActivityWithType(type: any, tag: string): void;
  function showActivityWithId(activityId: string): void;
  function removeTutorials(): void;
  function fetchInboxInfo(callback: (arg0: string) => any): void;
  function getUserBadges(callback: (arg0: string) => any): void;
  function registerForPush(): void;
  function sendDeviceToken(token: string): void;
  function sendPushDataToUpshot(pushPayload: string): void;
  function displayNotification(pushPayload: string): void;
  function disableUser(callback: (arg0: boolean) => any): void;
  function getUserId(callback: (arg0: string) => any): void;
  function getSDKVersion(callback: (arg0: string) => any): void;
  function getRewardsList(
    successCallback: (arg0: string) => any,
    failureCallback: (arg0: error) => any
  ): void;
  function getRewardHistoryForProgram(
    programId: string,
    historyType: Int,
    successCallback: (arg0: string) => any,
    failureCallback: (arg0: string) => any
  ): void;
  function getRewardRulesforProgram(
    programId: string,
    successCallback: (arg0: string) => any,
    failurecallback: any
  ): void;
  function redeemRewardsForProgram(
    programId: string,
    transactionValue: Int,
    redeemValue: Int,
    tag: string,
    successCallback: (arg0: string) => any,
    failurecallback: any
  ): void;
  function getPushClickPayload(callback: any): void;
  function getNotificationList(
    limit: number,
    loadMore: boolean,
    successCallback: (arg0: string) => any,
    failurecallback: any
  ): void;
  function getUnreadNotificationsCount(
    inboxType: any,
    callback: (arg0: number) => any
  ): void;
  function updateNotificationReadStatus(
    notificationId: string,
    callback: (arg0: response) => any
  ): void;
  function showInboxNotificationScreen(options: any): void;
  function getStreaksData(
    successCallback: (arg0: string) => any,
    failurecallback: any
  ): void;
  function addListener(eventName: string, handler: any): void;
  function removeEventListener(eventName: any): void;
}
export {
  UpshotActivityType,
  UpshotGender,
  UpshotMaritalStatus,
  UpshotRewardHistory,
  UpshotInitOptions,
  UpshotAttribution,
  UpshotInboxConfigOptions,
};
