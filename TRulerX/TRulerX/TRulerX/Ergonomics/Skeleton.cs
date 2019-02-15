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
                return GetUpperAngleGroup(BackAngle);
            }
        }
        public double ShoulderAngle { get; private set; }
        public int ShoulderAngleGroup
        {
            get
            {
                return GetUpperAngleGroup(ShoulderAngle);
            }
        }
        public double ElbowAngle { get; private set; }
        public int ElbowAngleGroup
        {
            get
            {
                return GetUpperAngleGroup(ElbowAngle);
            }
        }
        #endregion
        #region 하지 각도
        public double AnkleAngle { get; private set; }
        public int AnkleAngleGroup
        {
            get
            {
                return GetLowerAngleGroup(AnkleAngle);
            }
        }
        #endregion


        public Skeleton(Point[] points)
        {
            Points = points;

            BackAngle = 180 + GetAngle(Points[1], points[4], points[5]);
            ShoulderAngle = GetAngle(Points[4], points[1], points[2]);
            ElbowAngle = 90 + GetAngle(Points[1], points[2], points[3]);
            AnkleAngle = GetAngle(Points[4], points[5], points[6]);
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
            else if (angle >= -45 && angle < 15)
            {
                return 30;
            }
            else if(angle >= 15 && angle < 52.5)
            {
                return 45;
            }
            else if (angle >= 52.5 && angle < 67.5)
            {
                return 60;
            }
            else if (angle >= 67.5 && angle < 75)
            {
                return 71;
            }
            else if(angle >= 75 && angle < 107.5)
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

        private int GetUpperAngleGroup(double angle)
        {
            if (angle >= -22.5 && angle < 22.5)
            {
                return 0;
            }
            else if (angle >= 22.5 && angle < 67.5)
            {
                return 45;
            }
            else if (angle >= 67.5 && angle < 125)
            {
                return 90;
            }
            else
            {
                return GetAngleGroup(angle);
            }
        }

        private int GetLowerAngleGroup(double angle)
        {
            if (angle >= -22.5 && angle < 15)
            {
                return 0;
            }
            else if (angle >= 15 && angle < 45)
            {
                return 30;
            }
            else if (angle >= 45 && angle < 67.5)
            {
                return 60;
            }
            else if (angle >= 67.5 && angle < 105)
            {
                return 90;
            }
            else if (angle >= 105 && angle < 135)
            {
                return 120;
            }
            else if (angle >= 135 && angle < 165)
            {
                return 150;
            }
            else if (angle >= 165 && angle < 200)
            {
                return 180;
            }
            else
            {
                return GetAngleGroup(angle);
            }
        }
    }
}
