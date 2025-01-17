package ru.otus.homework.lintchecks

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import org.junit.Test

class RawColorDetectorTest {
    @Test
    fun `should detect raw colors`() {
        TestLintTask.lint()
            .allowMissingSdk()
            .detector(RawColorDetector())
            .issues(RawColorDetector.ISSUE)
            .files(
                TestFiles.xml(
                    "res/values/colors.xml",
                    """
                    <?xml version="1.0" encoding="utf-8"?>
                    <resources>
                        <color name="purple_200">#FFBB86FC</color>
                        <color name="purple_500">#FF6200EE</color>
                        <color name="purple_700">#FF3700B3</color>
                        <color name="teal_200">#FF03DAC5</color>
                        <color name="teal_700">#FF018786</color>
                        <color name="black">#FF000000</color>
                        <color name="white">#FFFFFFFF</color>
                    </resources>
                """.trimIndent()
                ).indented(),
                TestFiles.xml(
                    "res/layout/incorrect_color_usages_layout.xml",
                    """
                        <?xml version="1.0" encoding="utf-8"?>
                            <FrameLayout
                                xmlns:android="http://schemas.android.com/apk/res/android"
                                xmlns:app="http://schemas.android.com/apk/res-auto"
                                android:layout_width="match_parent"
                                android:layout_height="match_parent">
                            
                                <View
                                    android:id="@+id/case1"
                                    android:layout_width="80dp"
                                    android:layout_height="80dp"
                                    android:layout_marginTop="32dp"
                                    android:background="#FF3700B3"
                                    app:layout_constraintEnd_toEndOf="parent"
                                    app:layout_constraintStart_toStartOf="parent"
                                    app:layout_constraintTop_toTopOf="parent" />
                            
                                <View
                                    android:id="@+id/case2"
                                    android:layout_width="80dp"
                                    android:layout_height="80dp"
                                    android:layout_marginTop="16dp"
                                    android:background="#00bcd4"
                                    app:layout_constraintEnd_toEndOf="@+id/case1"
                                    app:layout_constraintStart_toStartOf="@+id/case1"
                                    app:layout_constraintTop_toBottomOf="@+id/case1" />
                            
                                <View
                                    android:id="@+id/case3"
                                    android:layout_width="80dp"
                                    android:layout_height="80dp"
                                    android:layout_marginTop="16dp"
                                    android:background="@android:color/holo_blue_dark"
                                    app:layout_constraintEnd_toEndOf="@+id/case2"
                                    app:layout_constraintStart_toStartOf="@+id/case2"
                                    app:layout_constraintTop_toBottomOf="@+id/case2" />
                            
                                <View
                                    android:id="@+id/case4"
                                    android:layout_width="80dp"
                                    android:layout_height="80dp"
                                    android:layout_marginTop="16dp"
                                    android:background="@color/teal_700"
                                    app:layout_constraintEnd_toEndOf="@+id/case3"
                                    app:layout_constraintStart_toStartOf="@+id/case3"
                                    app:layout_constraintTop_toBottomOf="@+id/case3" />
                            
                                <View
                                    android:id="@+id/case5"
                                    android:layout_width="80dp"
                                    android:layout_height="80dp"
                                    android:layout_marginTop="16dp"
                                    android:background="@color/selector"
                                    app:layout_constraintEnd_toEndOf="@+id/case4"
                                    app:layout_constraintStart_toStartOf="@+id/case4"
                                    app:layout_constraintTop_toBottomOf="@+id/case4" />
                            
                                <View
                                    android:id="@+id/case6"
                                    android:layout_width="80dp"
                                    android:layout_height="80dp"
                                    android:layout_marginTop="16dp"
                                    android:background="@drawable/ic_baseline_adb_24"
                                    android:backgroundTint="@color/purple_200"
                                    android:backgroundTintMode="add"
                                    app:layout_constraintEnd_toEndOf="@+id/case5"
                                    app:layout_constraintStart_toStartOf="@+id/case5"
                                    app:layout_constraintTop_toBottomOf="@+id/case5" />
                            
                            </FrameLayout>
                    """.trimIndent()
                ).indented()
            )
            .run()
            .expect(
                """res/layout/incorrect_color_usages_layout.xml:13: Warning: Используйте цвет из палитры colors.xml [RawColorUsage]
            android:background="#FF3700B3"
                                ~~~~~~~~~
res/layout/incorrect_color_usages_layout.xml:23: Warning: Используйте цвет из палитры colors.xml [RawColorUsage]
            android:background="#00bcd4"
                                ~~~~~~~
0 errors, 2 warnings""".trimIndent()
            )
    }
}