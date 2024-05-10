package org.example

data class AreaFigure(var height: Int, var width: Int) {
    operator fun plus(other: AreaFigure) = AreaFigure(height + other.height, width + other.width)
    operator fun minus(other: AreaFigure) = AreaFigure(height - other.height, width - other.width)

    operator fun plusAssign(other: AreaFigure) {
        height += other.height
        width += other.width
    }

    operator fun minusAssign(other: AreaFigure) {
        height -= other.height
        width -= other.width
    }

    operator fun inc() = AreaFigure(height * 2, width * 2)
    operator fun dec() = AreaFigure(0, 0)

    operator fun times(other: AreaFigure) = AreaFigure(height * other.height, width * other.width)
    operator fun div(other: AreaFigure) = AreaFigure(height / other.height, width / other.width)

    operator fun timesAssign(other: AreaFigure) {
        height *= other.height
        width *= other.width
    }

    operator fun divAssign(other: AreaFigure) {
        height /= other.height
        width /= other.width
    }

    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AreaFigure) return false
        return height * width == other.height * other.width
    }

    override fun hashCode(): Int {
        val result = (height * width).hashCode()
        return result
    }

    operator fun contains(other: AreaFigure) = height * width >= other.height * other.width

    operator fun compareTo(other: AreaFigure): Int {
        return height * width - other.height * other.width
    }

    operator fun get(getHeight: Int, getWidth: Int): Int {
        return getHeight * getWidth
    }

    operator fun rangeTo(endAreaFigure: AreaFigure): List<AreaFigure> {
        val result = ArrayList<AreaFigure>()
        for (newHeight in height..endAreaFigure.height) {
            for (newWidth in width..endAreaFigure.width) {
                result.add(AreaFigure(newHeight, newWidth))
            }
        }
        return result
    }
}

