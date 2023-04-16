package com.example.fitnesstracker

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var stepCounter: StepCounter
    private lateinit var totalStepsTextView: TextView
    private lateinit var morningStepsTextView: TextView
    private lateinit var dayStepsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalStepsTextView = findViewById(R.id.totalStepsTextView)
        morningStepsTextView = findViewById(R.id.morningStepsTextView)
        dayStepsTextView = findViewById(R.id.dayStepsTextView)
        stepCounter = StepCounter(this)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 0)
        } else {
            updateStepCounts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateStepCounts()
            }
        }
    }

    private fun updateStepCounts() {
        val totalSteps = stepCounter.getTotalSteps()
        val morningSteps = getMorningSteps()
        val daySteps = totalSteps - morningSteps

        totalStepsTextView.text = "Total Steps: $totalSteps"
        morningStepsTextView.text = "Morning Steps: $morningSteps"
        dayStepsTextView.text = "Day Steps: $daySteps"
    }

    private fun getMorningSteps(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis

        val currentTime = System.currentTimeMillis()

        val stepSensor = Sensor.TYPE_STEP_COUNTER
        val stepCount = stepCounter.getStepsSince(startTime, stepSensor)
        return stepCount
    }
}
