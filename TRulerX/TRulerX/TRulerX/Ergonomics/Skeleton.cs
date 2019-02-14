using System;
using System.Collections.Generic;
using System.Text;
using Xamarin.Forms;
using System.Diagnostics;

namespace TRulerX.Ergonomics
{
    public class Skeleton
    {
        private Point[] Points;

        #region 상지 각도
        public double BackAngle { get; private set; }
        public int BackAngleGroup
        {
            get
            {
                return GetAngleGroup(BackAngle);
            }
        }
        public double ShoulderAngle { get; private set; }
        public int ShoulderAngleGroup
        {
            get
            {
                return GetAngleGroup(ShoulderAngle);
            }
        }
        public double ElbowAngle { get; private set; }
        public int ElbowAngleGroup
        {
            get
            {
                return GetAngleGroup(ElbowAngle);
            }
        }
        #endregion
        #region
        public double AnkleAngle { get; private set; }
        public int AnkleAngleGroup
        {
            get
            {
                return GetAngleGroup(AnkleAngle);
            }
        }
        #endregion


        public Skeleton(Point[] points)
        {
            Points = points;

            BackAngle = 180 + GetAngle(Points[1], points[4], points[5]);
            ShoulderAngle = GetAngle(Points[4], points[1], points[2]);
            ElbowAngle = -GetAngle(Points[1], points[2], points[3]);
            AnkleAngle = 180 - GetAngle(Points[4], points[5], points[6]);
        }

        public double GetAngle(Point j1, Point midJ, Point j2)
        {
            double x1 = j1.X - midJ.X;
            double y1 = j1.Y - midJ.Y;
            double x2 = j2.Y - midJ.X;
            double y2 = j2.Y - midJ.Y;

            return Math.Atan2(y1 * x2 - x1 * y2, x1 * x2 + y1 * y2) * 180 / Math.PI;
        }

        private int GetAngleGroup(double angle)
        {
            if(angle >= -45 && angle < 22.5)
            {
                return 0;
            }
            else if(angle >= 22.5 && angle < 67.5)
            {
                return 45;
            }
            else if(angle >= 67.5 && angle < 107.5)
            {
                return 90;
            }
            else if (angle >= 107.5 && angle < 137.5)
            {
                return 125;
            }
            else if (angle >= 137.5 && angle < 170)
            {
                return 150;
            }
            else if (angle < 200)
            {
                return 180;
            }
            else
            {
                Debug.WriteLine("Warning: No angle group.");
                return -1;
            }
        }
    }
}
