/**
 * Created by pvnghe on 8/19/17.
 */

package pvnghe.patternslibrary.util;


import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pvnghe.patternslibrary.activity.MainActivity;
import pvnghe.patternslibrary.designpatterns.Task;

public class TextUtils {
    public static CharSequence addLinkPhoneNumber(final CharSequence text) {
        if (text == null || text.length() <=0) {
            return text;
        }
        Matcher phoneMatcher = Patterns.PHONE.matcher(text);
        Spannable phoneNumberSpannable = Spannable.Factory.getInstance().newSpannable(text);
        int nStart = 0, nEnd = 0;
        CharSequence phoneNumberCharSequence;
        while (phoneMatcher.find()) {
            int start = phoneMatcher.start(), end = phoneMatcher.end();
            if (start == nEnd) {
                nEnd = end;
            } else {
                if (nStart < nEnd) {
                    phoneNumberSpannable.setSpan(new ForegroundColorSpan(Color.RED), nStart, nEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    phoneNumberSpannable.setSpan(new StyleSpan(Typeface.BOLD), nStart, nEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    phoneNumberCharSequence = text.subSequence(nStart, nEnd);
                    final CharSequence finalPhoneNumberCharSequence = phoneNumberCharSequence;
                    phoneNumberSpannable.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Task.publish(new MainActivity.CallPhoneNumber(finalPhoneNumberCharSequence.toString()));
                        }
                    }, nStart, nEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                nStart = start;
                nEnd = end;
            }
        }

        if (nStart < nEnd) {
            phoneNumberSpannable.setSpan(new ForegroundColorSpan(Color.RED), nStart, nEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            phoneNumberSpannable.setSpan(new StyleSpan(Typeface.BOLD), nStart, nEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            phoneNumberCharSequence = text.subSequence(nStart, nEnd);
            final CharSequence finalPhoneNumberCharSequence = phoneNumberCharSequence;
            phoneNumberSpannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Task.publish(new MainActivity.CallPhoneNumber(finalPhoneNumberCharSequence.toString()));
                }
            }, nStart, nEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        Matcher phoneMatcher2 = Pattern.compile(Patterns.PHONE.pattern(), Pattern.CASE_INSENSITIVE).matcher(text);

        while (phoneMatcher2.find()) {
            int lStart = phoneMatcher2.start(), lEnd = phoneMatcher2.end();
            StyleSpan[] phoneNumberStyleSpans = phoneNumberSpannable.getSpans(lStart, lEnd, StyleSpan.class);
            for (StyleSpan span : phoneNumberStyleSpans) {
                if (span.getStyle() == Typeface.BOLD) {
                    int start = phoneNumberSpannable.getSpanStart(span);
                    int end = phoneNumberSpannable.getSpanEnd(span);
                    phoneNumberSpannable.removeSpan(span);

                    if (lStart > start) {
                        // phoneNumberSpannable.setSpan(new StyleSpan(Typeface.BOLD), start, lStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        phoneNumberCharSequence = text.subSequence(start, lStart);
                        final CharSequence finalPhoneNumberCharSequence = phoneNumberCharSequence;
                        phoneNumberSpannable.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Task.publish(new MainActivity.CallPhoneNumber(finalPhoneNumberCharSequence.toString()));
                            }
                        }, start, lStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }

                    if (end > lEnd) {
                        // phoneNumberSpannable.setSpan(new StyleSpan(Typeface.BOLD), lEnd, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        phoneNumberCharSequence = text.subSequence(lEnd, end);
                        final CharSequence finalPhoneNumberCharSequence = phoneNumberCharSequence;
                        phoneNumberSpannable.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Task.publish(new MainActivity.CallPhoneNumber(finalPhoneNumberCharSequence.toString()));
                            }
                        }, lEnd, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }
                }
            }
        }

        return phoneNumberSpannable;
    }
}
