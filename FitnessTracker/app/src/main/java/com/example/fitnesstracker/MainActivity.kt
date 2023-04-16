package com.example.fitnesstracker

import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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

        updateStepCounts()
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

