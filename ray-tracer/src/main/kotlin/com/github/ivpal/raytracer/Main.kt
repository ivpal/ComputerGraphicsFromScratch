package com.github.ivpal.raytracer

import java.io.PrintWriter

const val canvasWidth = 640
const val canvasHeight = 640
const val viewportWidth = 1
const val viewportHeight = 1
const val projectionPlaneDistance = 1.0f

val scene = listOf(
    Sphere(
        center = Vector3(0.0f, -1.0f, 3.0f),
        radius = 1.0f,
        color = Color.RED
    ),
    Sphere(
        center = Vector3(2.0f, 0.0f, 4.0f),
        radius = 1.0f,
        color = Color.BLUE
    ),
    Sphere(
        center = Vector3(-2.0f, 0.0f, 4.0f),
        radius = 1.0f,
        color = Color.GREEN
    ),
    Sphere(
        center = Vector3(0.0f, -5001.0f, 0.0f),
        radius = 5000.0f,
        color = Color(255, 255, 0)
    )
)

val lights = listOf(
    AmbientLight(0.2f),
    PointLight(0.6f, Vector3(2.0f, 1.0f, 0.0f)),
    DirectionLight(0.2f, Vector3(1.0f, 4.0f, 4.0f))
)

fun main() {
    val w = canvasWidth / 2
    val h = canvasHeight / 2

    val builder = StringBuilder("P3\n${canvasWidth} ${canvasHeight}\n255\n")
    for (y in (h - 1) downTo -h) {
        for (x in (w - 1) downTo -w) {
            val ray = canvasToViewport(x, y)
            val color = traceRay(ray, 1.0f, Float.POSITIVE_INFINITY)
            builder.append("${color.r} ${color.g} ${color.b}\n")
        }
    }

    PrintWriter("image.ppm").use { pw ->
        pw.write(builder.toString())
    }
}

fun canvasToViewport(x: Int, y: Int): Vector3 =
    Vector3(
        1.0f * x * viewportWidth / canvasWidth,
        1.0f * y * viewportHeight / canvasHeight,
        projectionPlaneDistance
    )

fun computeLighting(point: Vector3, normal: Vector3): Float {
    var intensity = 0.0f
    for (light in lights) {
        intensity += light.computeLighting(point, normal)
    }
    return intensity
}

fun traceRay(ray: Vector3, tMin: Float, tMax: Float): Color {
    var closestT = Float.POSITIVE_INFINITY
    var closestSphere: Sphere? = null
    for (sphere in scene) {
        val (t1, t2) = sphere.intersectRay(ray)
        if (t1 in tMin..tMax && t1 < closestT) {
            closestT = t1
            closestSphere = sphere
        }
        if (t2 in tMin..tMax && t2 < closestT) {
            closestT = t2
            closestSphere = sphere
        }
    }

    if (closestSphere == null) {
        return Color.WHITE
    }

    val point = Vector3.ORIGIN + ray * closestT
    val normal = point - closestSphere.center
    return closestSphere.color * computeLighting(point, normal.normalize())
}
