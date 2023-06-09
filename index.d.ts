export function initializeUpshot(): void;

export function initializeUpshotUsingOptions(options: String): void;

export function terminate(): void;

export function setDispatchInterval(interval: number): void;

export function createPageViewEvent(screenName: String, callback: Callback): void;

export function createCustomEvent(eventName: String, payload: String, isTimed: boolean, callback: Callback): void;

export function setValueAndClose(payload: String, eventId: String): void;

export function closeEventForId(eventId: String): void;

export function dispatchEventsWithTimedEvents(timed: boolean, callback: Callback): void;

export function createLocationEvent(latitude: String, longitude: String): void;

export function createAttributionEvent(payload: String, callback: Callback): void;

export function setUserProfile(userData: String, callback: Callback): void;

export function getUserDetails(callback: Callback): void;

export function showActivityWithType(type: UpshotActivityType, tag: String): void;

export function showActivityWithId(activityId: String): void;

export function removeTutorials(): void;

export function fetchInboxInfo(callback: Callback): void;

export function getUserBadges(callback: Callback): void;

export function registerForPush(callback: Callback): void;

export function sendDeviceToken(token: String): void;

export function sendPushDataToUpshot(pushPayload: String): void;

export function displayNotification(pushPayload: String): void;

export function disableUser(shouldDisable: boolean, callback: Callback): void;

export function getUserId(callback: Callback): void;

export function getSDKVersion(callback: Callback): void;

export function getRewardsList(successCallback: Callback, failureCallback: Callback): void;

export function getRewardHistoryForProgram(programId: String, historyType: number, successCallback: Callback, failureCallback: Callback): void;

export function getRewardRulesforProgram(programId: String, successCallback: Callback, failurecallback: Callback): void;

export function redeemRewardsForProgram(programId: String, transactionValue: number, redeemValue: number, tag: String, successCallback: Callback, failurecallback: Callback): void;

export function getPushClickPayload(callback: Callback): void;

export function addListener(eventName: string, handler: Function): void;

export function removeEventListener(eventName: string): void;


export const UpshotInitOptions = {
    AppId: "bkApplicationID",
    OwnerId: "bkApplicationOwnerID",
    ExternalStorage: "bkStorageAppMemory",
    EnableLocation: "bkFetchLocation",
    EnableCrashLog: "bkExceptionHandler"
}

export const UpshotActivityType = {
    any: -1,
    survey: 0,
    rating: 1,
    fullscreenAd: 3,
    opinionPoll: 5,
    tutorials: 7,
    inAppMessage: 8,
    badges: 9,
    screenTips: 10,
    trivia: 11,
    customActions: 12,
    miniGame: 13
}

export const UpshotGender = {

    male: 1,
    female: 2,
    other: 3,
    reset: 4
}

export const UpshotMaritalStatus = {

    single: 1,
    engaged: 2,
    married: 3,
    widow: 4,
    divorced: 5,
    Reset: 6,
}

export const UpshotRewardHistory = {
    entire: 0,
    earn: 1,
    expiry: 2,
    redeem: 3,
    negative: 4
}
