package com.example.fitnesstracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class StepCounter(context: Context) : SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = null
    private var stepCount: Int = 0

    init {
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun getTotalSteps(): Int {
        return stepCount
    }

    fun getStepsSince(startTime: Long, sensorType: Int): Int {
        val steps: Int = stepCount - getStepsAt(startTime, sensorType)
        return if (steps < 0) 0 else steps
    }

    private fun getStepsAt(time: Long, sensorType: Int): Int {
        var steps = 0
        val sensorData: List<SensorData>? = SensorDataStore.getSensorData(sensorType)
        sensorData?.forEach { sensor ->
            if (sensor.timestamp <= time) {
                steps = sensor.steps
            }
        }
        return steps
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            stepCount = event.values[0].toInt()
        }
    }

    fun registerListener() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }
}

data class SensorData(val timestamp: Long, val steps: Int)

object SensorDataStore {
    private val sensorData: MutableMap<Int, MutableList<SensorData>> = mutableMapOf()

    fun addSensorData(sensorType: Int, timestamp: Long, steps: Int) {
        val sensorDataList: MutableList<SensorData> = sensorData.getOrDefault(sensorType, mutableListOf())
        sensorDataList.add(SensorData(timestamp, steps))
        sensorData[sensorType] = sensorDataList
    }

    fun getSensorData(sensorType: Int): List<SensorData>? {
        return sensorData.get(sensorType)
    }
}