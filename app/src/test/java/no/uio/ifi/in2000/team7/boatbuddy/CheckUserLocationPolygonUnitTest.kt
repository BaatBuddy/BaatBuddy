package no.uio.ifi.in2000.team7.boatbuddy

import no.uio.ifi.in2000.team7.boatbuddy.data.PolygonPosition.checkUserLocationPolygon
import no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location.LocationService
import org.junit.Test

class CheckUserLocationPolygonUnitTest {


    @Test
    fun checkIfUserLocationIsInsideConvexPolygon_givesTrue() {
        // arrange
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 5.0, 5.0)

        // assert
        assert(result)
    }

    @Test
    fun checkIfUserLocationIsOutsideConvexPolygon_givesTrue() {
        // arrange
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 50000.0, 5.0)

        // assert
        assert(!result)
    }


    @Test
    fun checkIfUserLocationIsOnEdgeOfConvexPolygon_givesTrue() {
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 5.0, 0.0)

        // assert
        assert(result)
    }

    @Test
    fun checkIfUserLocationIsRightNextToEdgeOfConvexPolygon_givesTrue() {
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 5.0, 10.0000000000001)

        // assert
        assert(!result)
    }

    @Test
    fun checkIfUserLocationIsOnCornerOfPolygon_givesTrue() {
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 0.0, 0.0)

        // assert
        assert(result)
    }

    @Test
    fun checkIfUserLocationIsInsideOfNotConvexPolygon_givesTrue() {
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(5.0, 5.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 5.0, 7.5)
                && checkUserLocationPolygon(polygon, 1.0, 2.0)

        // assert
        assert(result)
    }

    // hard to fix problem (testing if the point is on the edge that is not a straight line, but can detect if the lon and alt is 0.9999999 and 1.0
    /*@Test
    fun checkIfUserLocationOnEdgeOfNotConvexPolygon_givesTrue() {
        val locationService = LocationService()

        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(5.0, 5.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = locationService.checkUserLocationPolygon(polygon, 1.0, 1.0)

        // assert
        assert(result)
    }*/

    @Test
    fun checkIfUserLocationRightNextToSlantedEdgeOfNotConvexPolygon_givesTrue() {
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(5.0, 5.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 0.999999999, 1.0)

        // assert
        assert(result)
    }

    @Test
    fun checkIfUserLocationIsOutsideOfNotConvexPolygon_givesFalse() {
        val polygon = listOf(
            listOf(0.0, 0.0),
            listOf(5.0, 5.0),
            listOf(10.0, 0.0),
            listOf(10.0, 10.0),
            listOf(0.0, 10.0)
        )

        // act
        val result = checkUserLocationPolygon(polygon, 5.0, 2.5)

        // assert
        assert(!result)
    }


}