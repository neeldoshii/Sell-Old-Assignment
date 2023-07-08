package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class FirstFragment : Fragment() {


    private var selectedSoundUri: Uri? = null
    private lateinit var btnSelectSound: Button
    private lateinit var etTime: EditText
    private lateinit var btnPlayNotification: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_first, container, false)

        btnSelectSound= view.findViewById(R.id.btnSelectSound)
        etTime = view.findViewById(R.id.etTime)
        btnPlayNotification = view.findViewById(R.id.btnPlayNotification)

        // when select btn tapped
        btnSelectSound.setOnClickListener {
            selectNotificationSound()
        }

        //when play btn tapped
        btnPlayNotification.setOnClickListener {
            startCountdown(etTime.text.toString().toLong())

        }

        return view
    }

    private fun selectNotificationSound() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        startActivityForResult(intent, REQUEST_CODE_SELECT_SOUND)
    }

    private fun playNotification() {
        val timeInSeconds = etTime.text.toString().toIntOrNull()


            // Play the selected sound for the specified time
            val ringtone = RingtoneManager.getRingtone(requireContext(), selectedSoundUri)
            ringtone.play()

            // Stop playing after the specified time
            Thread {
                if (timeInSeconds != null) {
                    Thread.sleep(timeInSeconds * 1000L)
                }
                ringtone.stop()
            }.start()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_SOUND && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            selectedSoundUri = uri
        }
    }

    companion object {
        private const val REQUEST_CODE_SELECT_SOUND = 1
    }




    //countdown fun for notification sound to play
    fun startCountdown(seconds: Long) {
        object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
                // Update UI or perform any desired action with the remaining seconds
                val test : TextView = view!!.findViewById(R.id.test)
                test.text = "TimeRemaining : "+ remainingSeconds.toString()
//                Toast.makeText(view?.context, remainingSeconds.toString(), Toast.LENGTH_SHORT).show()
                if(remainingSeconds == 0L){
//                    Toast.makeText(view?.context, "Playing", Toast.LENGTH_SHORT).show()
                    playNotification()
                }
                println("Remaining seconds: $remainingSeconds")
            }


            override fun onFinish() {
                // Countdown finished, perform any desired action
                println("Countdown finished!")
            }
        }.start()
    }


}