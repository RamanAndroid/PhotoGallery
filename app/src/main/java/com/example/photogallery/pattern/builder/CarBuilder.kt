package com.example.photogallery.pattern.builder

class CarBuilder {
    private var typeCar: CarType = CarType.SEDAN
    private var seats: Int = 1
    private var transmission: Transmission = MechanicTransmission()
    private var tripComputer: TripComputer? = null
    private var gpsNavigator: GPSNavigator? = null
    private var engine: Engine = DieselEngine()

    fun setCarType(type: CarType): CarBuilder {
        this.typeCar = type

        return this
    }

    fun setSeats(seats: Int): CarBuilder {
        this.seats = seats

        return this
    }

    fun setEngine(engine: Engine): CarBuilder {
        this.engine = engine

        return this
    }

    fun setTransmission(transmission: Transmission): CarBuilder {
        this.transmission = transmission

        return this
    }

    fun setTripComputer(tripComputer: TripComputer): CarBuilder {
        this.tripComputer = tripComputer

        return this
    }

    fun setGPSNavigator(gpsNavigator: GPSNavigator): CarBuilder {
        this.gpsNavigator = gpsNavigator

        return this
    }

    fun build(): Car {
        return Car(typeCar, seats, transmission, tripComputer, gpsNavigator, engine)
    }
}

data class Car(
    val type: CarType,
    val seats: Int,
    val transmission: Transmission,
    val tripComputer: TripComputer? = null,
    val gpsNavigator: GPSNavigator? = null,
    val engine: Engine
)


open class Engine
class DieselEngine : Engine()
class ElectricEngine : Engine()
open class Transmission
class AutomaticTransmission : Transmission()
class MechanicTransmission : Transmission()
class TripComputer
class GPSNavigator
enum class CarType {
    SEDAN(),
    RACE(),
    JEEP()
}