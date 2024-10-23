package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<>(LoginActivity.class);

    private  View decorView;

    @Before
    public void setUp() {
        activityRule.getActivity();

        // Xóa dữ liệu SharedPreferences cũ trước khi chạy test
        InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences("LoginPrefs", 0)
                .edit().clear().apply();
    }

    @Test
    public void testLoginSuccess()  {
        onView(withId(R.id.edtUsername)).perform(typeText("table1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.edtPassword)).perform(typeText("pass1"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.btnLogin)).perform(click());
//        Thread.sleep(2000);
        onView(withId(R.id.txtErrorText))
                .check(matches(isDisplayed()))
                .check(matches(withText("Đăng nhập thành công!")));
    }

    @Test
    public void testLoginFailureForNotMatch() {
        // Nhập username và password không đúng
        onView(withId(R.id.edtUsername)).perform(typeText("wrongUser"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.edtPassword)).perform(typeText("wrongPassword"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.txtErrorText))
                .check(matches(isDisplayed())) // Kiểm tra xem TextView có được hiển thị không
                .check(matches(withText("Sai tên đăng nhập hoặc mật khẩu!"))); // Kiểm tra nội dung text

//        onView(withId(R.id.txtErrorText)).perform()
//        onView(withId(R.id.txtErrorText))
//                .check(matches(withText("Sai tên đăng nhập hoặc mật khẩu!")));
    }
    @Test
    public void testLoginFailureForEmpty() {
        // Nhập username và password không đúng
        onView(withId(R.id.edtUsername)).perform(typeText(""), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.edtPassword)).perform(typeText(""), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.txtErrorText))
                .check(matches(isDisplayed()))
                .check(matches(withText("Vui lòng nhập đầy đủ thông tin!")));
    }
}
