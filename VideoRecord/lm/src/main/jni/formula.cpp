//
// Created by 李敏 on 2016/07/18.
//

#include "formula.h"
#include <math.h>

#define X_PI (3.14159265358979324 * 3000.0 / 180.0)

double trans_from_latitude(double x, double y) {
	double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(fabs(x));
	ret += (20.0 * sin(6.0 * x * M_PI) + 20.0 * sin(2.0 * x * M_PI)) * 2.0 / 3.0;
	ret += (20.0 * sin(y * M_PI) + 40.0 * sin(y / 3.0 * M_PI)) * 2.0 / 3.0;
	ret += (160.0 * sin(y / 12.0 * M_PI) + 320 * sin(y * M_PI / 30.0)) * 2.0 / 3.0;
	return ret;
}

double trans_from_longitude(double x, double y) {
	double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(fabs(x));
	ret += (20.0 * sin(6.0 * x * M_PI) + 20.0 * sin(2.0 * x * M_PI)) * 2.0 / 3.0;
	ret += (20.0 * sin(x * M_PI) + 40.0 * sin(x / 3.0 * M_PI)) * 2.0 / 3.0;
	ret += (150.0 * sin(x / 12.0 * M_PI) + 300.0 * sin(x / 30.0 * M_PI)) * 2.0 / 3.0;
	return ret;
}

void delta(double latitude, double longitude, double *latitude_out, double *longitude_out) {
	double a = 6378245.0;
	double ee = 0.00669342162296594323;
	double dLat = trans_from_latitude(longitude - 105.0, latitude - 35.0);
	double dLon = trans_from_longitude(longitude - 105.0, latitude - 35.0);
	double radLat = latitude / 180.0 * M_PI;
	double magic = sin(radLat);
	magic = 1 - ee * magic * magic;
	double sqrtMagic = sqrt(magic);

	*latitude_out = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * M_PI);
	*longitude_out = (dLon * 180.0) / (a / sqrtMagic * cos(radLat) * M_PI);
}

int out_of_china(double latitude, double longitude) {
	if (longitude < 72.004 || longitude > 137.8347)
		return 1;

	if (latitude < 0.8293 || latitude > 55.8271)
		return 1;

	return 0;
}


void wgs84_2_gcj02(double latitude, double longitude, double *latitude_out, double *longitude_out) {
	if (out_of_china(latitude, longitude)) {
		*latitude_out = latitude;
		*longitude_out = longitude;
	}
	else {
		double lat, lon;
		delta(latitude, longitude, &lat, &lon);
		*latitude_out = lat + latitude;
		*longitude_out = lon + longitude;
	}
}

void gcj02_2_wgs84(double latitude, double longitude, double *latitude_out, double *longitude_out) {
	if(out_of_china(latitude, longitude)) {
		*latitude_out = latitude;
		*longitude_out = longitude;
	}
	else {
		double lat, lon;
		delta(latitude, longitude, &lat, &lon);
		*latitude_out = latitude - lat;
		*longitude_out = longitude - lon;
	}
}

void gcj02_2_wgs84exa(double latitude, double longitude, double *latitude_out, double *longitude_out) {
	double initDelta = 0.01;
	double threshold = 0.000000001;
	double dLat = initDelta, dLon = initDelta;
	double mLat = latitude - dLat, mLon = longitude - dLon;
	double pLat = latitude + dLat, pLon = longitude + dLon;
	double wgsLat, wgsLon, i = 0;
	while(1) {
		wgsLat = (mLat + pLat) / 2;
		wgsLon = (mLon + pLon) / 2;

		double lat, lon;
		gcj02_2_wgs84(latitude, longitude, &lat, &lon);
		dLat = lat - latitude;
		dLon = lon - longitude;
		if ((fabs(dLat) < threshold) && (fabs(dLon) < threshold))
			break;

		if (dLat > 0) pLat = wgsLat; else mLat = wgsLat;
		if (dLon > 0) pLon = wgsLon; else mLon = wgsLon;

		if (++i > 10000) break;
	}

	*latitude_out = wgsLat;
	*longitude_out = wgsLon;
}

void gcj02_2_bd09(double latitude, double longitude, double *latitude_out, double *longitude_out) {
	double x = longitude, y = latitude;
	double z = sqrt(x * x + y * y) + 0.00002 * sin(y * X_PI);
	double theta = atan2(y, x) + 0.000003 * cos(x * X_PI);

	*longitude_out = z * cos(theta) + 0.0065;
	*latitude_out = z * sin(theta) + 0.006;
}

void bd09_2_gcj02(double latitude, double longitude, double *latitude_out, double *longitude_out) {
	double x = latitude - 0.0065, y = longitude - 0.006;
	double z = sqrt(x * x + y * y) - 0.00002 * sin(y * X_PI);
	double theta = atan2(y, x) - 0.000003 * cos(x * X_PI);
	*longitude_out = z * cos(theta);
	*latitude_out = z * sin(theta);
}

double distance(double latitude_a, double longitude_a, double latitude_b, double longitude_b) {
	double earthR = 6371000;
	double x = cos(latitude_a * M_PI/180) * cos(latitude_b * M_PI / 180) * cos((longitude_a - longitude_b) * M_PI / 180);
	double y = sin(latitude_a * M_PI/180) * sin(latitude_b * M_PI / 180);
	double s = x + y;
	if (s > 1) s = 1;
	if (s < -1) s = -1;
	double alpha = acos(s);
	return alpha * earthR;
}