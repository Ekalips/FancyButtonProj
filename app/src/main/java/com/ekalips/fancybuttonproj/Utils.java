package com.ekalips.fancybuttonproj;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by wldev on 1/18/17.
 */

public class Utils {
    public static Path composeRoundedRectPath(float left, float top, float right, float bottom, float diameter){
        Path path = new Path();
        path.moveTo(left + diameter/2 ,top);
        path.lineTo(right - diameter/2,top);
        path.quadTo(right, top, right, top + diameter/2);
        path.lineTo(right ,bottom - diameter/2);
        path.quadTo(right ,bottom, right - diameter/2, bottom);
        path.lineTo(left + diameter/2,bottom);
        path.quadTo(left,bottom,left, bottom - diameter/2);
        path.lineTo(left,top + diameter/2);
        path.quadTo(left,top, left + diameter/2, top);
        path.close();
        return path;
    }

    public static Path composeRoundedRectPath(RectF rectF, float diameter){
        return composeRoundedRectPath(rectF,diameter,diameter,diameter,diameter);
    }

    public static Path composeRoundedRectPath(RectF rect, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter){
        Path path = new Path();
        topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
        topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
        bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
        bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

        path.moveTo(rect.left + topLeftDiameter/2 ,rect.top);
        path.lineTo(rect.right - topRightDiameter/2,rect.top);
        path.quadTo(rect.right, rect.top, rect.right, rect.top + topRightDiameter/2);
        path.lineTo(rect.right ,rect.bottom - bottomRightDiameter/2);
        path.quadTo(rect.right ,rect.bottom, rect.right - bottomRightDiameter/2, rect.bottom);
        path.lineTo(rect.left + bottomLeftDiameter/2,rect.bottom);
        path.quadTo(rect.left,rect.bottom,rect.left, rect.bottom - bottomLeftDiameter/2);
        path.lineTo(rect.left,rect.top + topLeftDiameter/2);
        path.quadTo(rect.left,rect.top, rect.left + topLeftDiameter/2, rect.top);
        path.close();

        return path;
    }
}
