# 高德/Google 地图SDK使用小结

## 高德SDK
1. 覆盖物
```java
 //绘制marker
    Marker marker = aMap.addMarker(new MarkerOptions()
        .position(new LatLng(39.986919,116.353369))
        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
            .decodeResource(getResources(),R.drawable.marker)))
        .draggable(true));

    // 绘制曲线
    aMap.addPolyline((new PolylineOptions())
        .add(new LatLng(43.828, 87.621), new LatLng(45.808, 126.55))
        .geodesic(true).color(Color.RED));
```

2. 路径规划
```java
    final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
        mStartPoint, mEndPoint);
    if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
        BusRouteQuery query = new BusRouteQuery(fromAndTo, mode,
            mCurrentCityName, 0);
        mRouteSearch.calculateBusRouteAsyn(query);
    } else if (routeType == ROUTE_TYPE_DRIVE) {
        DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    } else if (routeType == ROUTE_TYPE_WALK) {
        WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
        mRouteSearch.calculateWalkRouteAsyn(query);
    }
```

3. POI 搜索
```java
/**
 * 开始进行poi搜索
 */
protected void doSearchQuery() {
    keyWord = mSearchText.getText().toString().trim();
    currentPage = 0;
    query = new PoiSearch.Query(keyWord, "", "北京市");
    query.setPageSize(20);// 设置每页最多返回多少条poiitem
    query.setPageNum(currentPage);// 设置查第一页

    if (lp != null) {
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new SearchBound(lp, 5000, true));//
        poiSearch.searchPOIAsyn();// 异步搜索
    }
}
```
4. 坐标转换
```java
CoordinateConverter converter  = new CoordinateConverter(); 
converter.from(CoordType.GPS); 
converter.coord(sourceLatLng); 
LatLng desLatLng = converter.convert();
```
5. 经纬度坐标与屏幕像素坐标互转

| 名称        | 说明           |
| ------------- |:-------------:|
|AMap.getProjection().fromScreenLocation(android.graphics.Point paramPoint)|根据转入的屏幕位置返回一个地图位置（经纬度）|
| AMap.getProjection().toScreenLocation(LatLng paramLatLng) |返回一个从地图位置转换来的屏幕位置|
| AMap.getProjection().toOpenGLWidth(int screenWidth)|返回一个屏幕宽度转换来的openGL 需要的宽度|
| AMap.getProjection().toOpenGLLocation(LatLng paramLatLng)|返回一个从经纬度坐标转换来的opengles 需要的坐标|

## 高德 JS SDK调起高德地图
>1. 点标记
>2. POI标记
>3. 路径规划

## 谷歌map 
#### 地图SDK
1. 绘制marker
```java

//
@Override
public void onMapReady(GoogleMap map) {
    map.addMarker(new MarkerOptions()
        .position(new LatLng(10, 10))
        .title("Hello world"));
}

```
2. 绘制曲线
```java
PolylineOptions rectOptions = new PolylineOptions()
        .add(new LatLng(37.35, -122.0))
        .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
        .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
        .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
        .add(new LatLng(37.35, -122.0)); // Closes the polyline.
Polyline polyline = myMap.addPolyline(rectOptions);
```
#### 3. 工具
1. 将 GeoJSON 导入您的地图
>您可以 GeoJSON 格式存储特征，然后使用此实用程序将它们渲染为覆盖在地图上的叠层。调用 addLayer() 可向地图添加您的 GeoJSON 数据。您还可以通过调用 addFeature()，传入 GeoJsonFeature 对象来添加个别特征。
2. 将 KML 导入您的地图
>您可以利用该实用程序将 KML 对象转换为地理形状，并将它们渲染为覆盖在地图上的叠层。调用 addLayerToMap() 可向地图添加您的叠层。您可以通过对任何 Placemark、GroundOverlay、Document 或 Folder 调用 getProperties() 来访问 KML 对象中的属性。
3. 向您的地图添加热图
>热图便于查看者了解地图上数据点的分布和相对密度。热图不是在每个位置放置标记，而是利用颜色和形状来表示数据的分布。创建一个 HeatmapTileProvider，向其传递 LatLng 对象集，其中的对象表示地图上的关注点。然后新建一个 TileOverlay，向其传递热图图块提供程序，然后向地图添加图块叠层。

4. 通过气泡图标定制标记
>添加一个 IconGenerator，以便在标记上显示零星信息。此实用程序可让您的标记图标有些类似于信息窗口，因为标记本身就包含文本和其他内容。其优点在于，您可以让多个标记同时保持打开状态，而信息窗口则同时只能打开一个。您还可以设置标记样式、更改标记和/或内容的朝向以及更改标记的背景图像/九宫格图像。

5. 管理标记聚类
>ClusterManager 可帮助您管理不同缩放比例的多个标记。也就是说，即便您在地图上放置大量标记，也不会使地图变得难以查看。当用户在高缩放比例下查看地图时，各个标记会显示在地图上。当用户缩小到较低缩放比例时，这些标记会聚拢形成聚类，以方便地图的查看。

6. 编码和解码多段线
> PolyUtil 用于在编码多段线和多边形与纬度/经度坐标之间进行双向转换。
在 Google 地图中，定义多段线或多边形的纬度和经度坐标以编码字符串形式存储。请参阅对多段线编码的详细说明。您收到的编码字符串可能来自 Google Maps Directions API 等 Google API 的响应。
您可以利用 Google Maps Android API Utility Library 中的 PolyUtil 将纬度/经度坐标序列（“LatLngs”）编码成一个编码路径字符串，以及将编码路径字符串解码成 LatLngs 序列。这可以确保与 Google Maps API 网络服务的互操作性。

7. 	通过球面几何学计算距离、面积和方位
>您可以利用 SphericalUtil 中的球面几何学实用程序根据纬度和经度计算距离、面积和方位。以下是该实用程序中提供的部分方法：
>+ computeDistanceBetween() – 返回两个纬度/经度坐标之间的距离（单位：米）。
>+ computeHeading() – 返回两个纬度/经度坐标之间的方位（单位：度）。
>+ computeArea() – 返回地球上某个闭合路径的面积（单位：平方米）。
>+ interpolate() – 返回位于两个给定点之间几分之一距离处的点的纬度/经度坐标。举例来说，您可以利用它以动画方式呈现两点间的标记。
