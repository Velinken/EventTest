package com.dinadurykina.eventtest.ui.fragment

// build.gradle(Module): dependencies.implementation 'androidx.fragment:fragment-ktx:1.3.5'
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.dinadurykina.eventtest.R
import com.dinadurykina.eventtest.databinding.FragmentBinding
import com.dinadurykina.eventtest.util.EventObserver
import com.dinadurykina.eventtest.util.observeEvent
import com.google.android.material.snackbar.Snackbar

/**
 * В техгологии JetPack [Fragment] вызывается из MainActivity:
 * класс [Fragment], построит и высветит очередной экран на смартфоне
 * размещается обычно на весь экран (не обязательно, см FragmentContainerView)
 * макет экрана строит (надувает) обычно из своего fragment.xml
 * согласно JetPack должен обладать технологией buildFeatures.dataBinding = true
 * согласно JetPack должен создавать под собой FragmentViewModel.kt
 */

class Fragment : Fragment() {

    // [Fragment] создает viewModel используя fragment-ktx
    private val viewModel: FragmentViewModel by viewModels()
    private lateinit var binding: FragmentBinding  // из fragment.xml

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Простейший стандартный способ в JetPack обеспечения [Fragment]:
        binding = FragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    // В этом блоке навешиваем слушателей событий происходящих в viewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toast.observeEvent(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            binding.message.text = viewModel.toast.value?.peekContent() ?: "nul"
        }
        // ПОСЛЕ СРАБОТКИ Trigger ЕГО НЕ НАДО ОПУСКАТЬ - это произведется автоматически
        // т.е. не нужна функция опускания флажка и ее вызов из фрагмента
        // Другими словами Event + observeEvent фактически это SingleEventObserver
        // второй раз наблюдатель сработать на флажке не может - Trigger уже опущен

        viewModel.keyBoard.observeEvent(viewLifecycleOwner) {
            when (it) {
                true -> showKeyboard()
                false -> hideKeyboard()
            }
            binding.message.text = (viewModel.keyBoard.value?.peekContent() ?: "nul").toString()
        }

        viewModel.snackbar.observeEvent(viewLifecycleOwner) {
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
            binding.message.text = viewModel.snackbar.value?.peekContent()?:"nul"
        }

        // Пример альтернативного наблюдателя (class EventObserver шлет уведомления)
       viewModel.notify.observe(viewLifecycleOwner, EventObserver {text ->
           val NOTIFICATION_CHANNEL_ID = "101"
           val NOTIFICATION_CHANNEL_NAME = "CANALID"

           val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           if (Build.VERSION.SDK_INT >= 26) {
               val channel = NotificationChannel(
                   NOTIFICATION_CHANNEL_ID,
                   NOTIFICATION_CHANNEL_NAME,
                   NotificationManager.IMPORTANCE_DEFAULT
               )
               notificationManager.createNotificationChannel(channel)
           }

           val pendingIntent = PendingIntent.getActivity(activity, 0, Intent(), 0)
           val notification = NotificationCompat.Builder(requireContext(),NOTIFICATION_CHANNEL_ID)
               .setContentTitle("event notification title")
               .setContentText(text)
               .setSmallIcon(R.mipmap.ic_launcher)
               //.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
               .setAutoCancel(true)
               //.setPriority(NotificationCompat.PRIORITY_DEFAULT)
           notification.setContentIntent(pendingIntent)
           notificationManager.notify(0, notification.build())

                binding.message.text = viewModel.notify.value?.peekContent() ?: "nul"
        })
    }

    private fun showKeyboard () =
        binding.apply {
            keyboard.requestFocus()
            getSystemService(keyboard.context, InputMethodManager::class.java)!!
                .showSoftInput(keyboard, 0)
        }

    private fun hideKeyboard() =
        binding.apply {
            invalidateAll()   // обновить экран
            keyboard.requestFocus()
            getSystemService(keyboard.context, InputMethodManager::class.java)!!
             .hideSoftInputFromWindow(keyboard.windowToken, 0)
        }
}
