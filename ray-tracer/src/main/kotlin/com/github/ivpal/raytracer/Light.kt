package com.github.ivpal.raytracer

sealed class Light(val intensity: Float) {
    class AmbientLight(intensity: Float): Light(intensity)
    class PointLight(val position: Vector3, intensity: Float): Light(intensity)
    class DirectionLight(val direction: Vector3, intensity: Float): Light(intensity)
}
