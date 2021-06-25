package com.dinadurykina.eventtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinadurykina.eventtest.databinding.MainActivityBinding
/**
 * Создается новый класс MainActivity с родителем AppCompatActivity
 * Используется viewBinding, для этого в build.gradle должно стоять:
 * buildFeatures.viewBinding = true
 *
 * В примере используется Google material desing -> необходимо
 * dependencies.implementation 'com.google.android.material:material:1.3.0'
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Надувает главный экран из main_activity.xml и отдает на высветку
        setContentView(MainActivityBinding.inflate(layoutInflater).root)
    }
}
/**
 * Андроид получив на высветку main_activity.xml и, найдя там FragmentContainerView,
 * Берет из него android:name="com.dinadurykina.eventtest.ui.fragment.Fragment"
 * и загружает указанный фрагмент нг это место экрана. Дальше выполняется фрагмент.
 */

/**
 * Можно не иметь main_activity.xml совсем и следовательно не нужен viewBinding, но тогда
 * 1. Создать здесь котлином FragmentContainerView с необходимыми полями
 * 2. загрузить туда фрагмент менеджером eventtest.ui.fragment.Fragment
 * 3. отдать Андроиду на высветку
 * Не в этом примере
 */