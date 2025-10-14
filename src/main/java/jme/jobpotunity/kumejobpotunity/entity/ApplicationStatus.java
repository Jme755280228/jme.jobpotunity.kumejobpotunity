package jme.jobpotunity.kumejobpotunity.entity;

/**
 * ApplicationStatus Enum
 * လျှောက်လွှာအခြေအနေများကို ဖော်ပြရာမှာ သုံးပါသည်။
 */
public enum ApplicationStatus {

    /** လျှောက်လွှာ တင်သွင်းပြီး */
    APPLIED,

    /** စိစစ်နေဆဲ */
    REVIEWING,

    /** ရွေးချယ်ခံရ (အင်တာဗျူး သို့မဟုတ် နောက်တစ်ဆင့်) */
    SHORTLISTED,

    /** ပယ်ချခံရ */
    REJECTED,

    /** အလုပ်ခန့်အပ် */
    HIRED
}
