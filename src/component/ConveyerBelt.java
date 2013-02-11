package component;


public class ConveyerBelt {

    private int xx1, yy1, xx2, yy2;
    private int direction;          // 1, 2, 3, 4 theo chieu kim dong ho bat dau tu goc 1h
    private int length;             // phai lon hon 1

    public ConveyerBelt(int passedXx1, int passedYy1, int passedXx2, int passedYy2) {
        xx1 = passedXx1;
        yy1 = passedYy1;
        xx2 = passedXx2;
        yy2 = passedYy2;

        // sinh direction
        if (xx1 == xx2) {
            if (yy1 < yy2) {
                direction = 2;
                length = yy2 - yy1 + 1;
            } else {
                direction = 4;
                length = yy1 - yy2 + 1;
            }
        }
        if (yy1 == yy2) {
            if (xx1 < xx2) {
                direction = 1;
                length = xx2 - xx1 + 1;
            } else {
                direction = 3;
                length = xx1 - xx2 + 1;
            }
        }
    }

    public void setXx1(int val) {
        xx1 = val;
    }

    public int getXx1() {
        return xx1;
    }

    public void setYy1(int val) {
        yy1 = val;
    }

    public int getYy1() {
        return yy1;
    }

    public void setXx2(int val) {
        xx2 = val;
    }

    public int getXx2() {
        return xx2;
    }

    public void setYy2(int val) {
        yy2 = val;
    }

    public int getYy2() {
        return yy2;
    }

    public int getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public boolean cross(ConveyerBelt another) {
        boolean result = false;
        if ((direction % 2) != (another.direction % 2)) {
            if (direction % 2 == 0) {
                if (((another.xx1 - xx1) * (another.xx2 - xx1) <= 0) && ((yy1 - another.yy1) * (yy2 - another.yy1)) <= 0) {
                    result = true;
                }
            } else {
                if (((xx1 - another.xx1) * (xx2 - another.xx1) <= 0) && ((another.yy1 - yy1) * (another.yy2 - yy1) <= 0)) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     *
     * toa do xx cua diem cross
     */
    public int crossXx(ConveyerBelt another) {
        int result = -1;
        if ((direction % 2) != (another.direction % 2)) {
            if (direction % 2 == 0) {
                if (((another.xx1 - xx1) * (another.xx2 - xx1) <= 0) && ((yy1 - another.yy1) * (yy2 - another.yy1)) <= 0) {
                    result = xx1;
                }
            } else {
                if (((xx1 - another.xx1) * (xx2 - another.xx1) <= 0) && ((another.yy1 - yy1) * (another.yy2 - yy1) <= 0)) {
                    result = another.xx1;
                }
            }
        }
        return result;
    }

    /**
     *
     * toa do yy cua diem cross
     */
    public int crossYy(ConveyerBelt another) {
        int result = -1;
        if ((direction % 2) != (another.direction % 2)) {
            if (direction % 2 == 0) {
                if (((another.xx1 - xx1) * (another.xx2 - xx1) <= 0) && ((yy1 - another.yy1) * (yy2 - another.yy1)) <= 0) {
                    result = another.yy1;
                }
            } else {
                if (((xx1 - another.xx1) * (xx2 - another.xx1) <= 0) && ((another.yy1 - yy1) * (another.yy2 - yy1) <= 0)) {
                    result = yy1;
                }
            }
        }
        return result;
    }

    public boolean overlapSameDirection(ConveyerBelt another) {
        boolean result = false;
        if (direction == another.direction) {
            switch (direction) {
                case 2:
                    if (xx1 == another.xx1) {
                        if (yy1 <= another.yy1) {
                            if (another.yy1 <= yy2) {
                                result = true;
                            }
                        } else {
                            if (yy1 <= another.yy2) {
                                result = true;
                            }
                        }
                    }
                    break;

                case 3:
                    if (yy1 == another.yy1) {
                        if (xx1 >= another.xx1) {
                            if (another.xx1 >= xx2) {
                                result = true;
                            }
                        } else {
                            if (xx1 >= another.xx2) {
                                result = true;
                            }
                        }
                    }
                    break;

                case 4:
                    if (xx1 == another.xx1) {
                        if (yy1 >= another.yy1) {
                            if (another.yy1 >= yy2) {
                                result = true;
                            }
                        } else {
                            if (yy1 >= another.yy2) {
                                result = true;
                            }
                        }
                    }
                    break;

                case 1:
                    if (yy1 == another.yy1) {
                        if (xx1 <= another.xx1) {
                            if (another.xx1 <= xx2) {
                                result = true;
                            }
                        } else {
                            if (xx1 <= another.xx2) {
                                result = true;
                            }
                        }
                    }
                    break;
            }
        }
        return result;
    }

    /**
     *
     * toa do xx cua dau start
     */
    public int overlapSameDirectionXx1(ConveyerBelt another) {
        int result = -1;
        if (direction == another.direction) {
            switch (direction) {
                case 2:
                    if (xx1 == another.xx1) {
                        if (yy1 <= another.yy1) {
                            if (another.yy1 <= yy2) {
                                result = xx1;
                            }
                        } else {
                            if (yy1 <= another.yy2) {
                                result = xx1;
                            }
                        }
                    }
                    break;

                case 3:
                    if (yy1 == another.yy1) {
                        if (xx1 >= another.xx1) {
                            if (another.xx1 >= xx2) {
                                result = xx1;
                            }
                        } else {
                            if (xx1 >= another.xx2) {
                                result = another.xx1;
                            }
                        }
                    }
                    break;

                case 4:
                    if (xx1 == another.xx1) {
                        if (yy1 >= another.yy1) {
                            if (another.yy1 >= yy2) {
                                result = xx1;
                            }
                        } else {
                            if (yy1 >= another.yy2) {
                                result = xx1;
                            }
                        }
                    }
                    break;

                case 1:
                    if (yy1 == another.yy1) {
                        if (xx1 <= another.xx1) {
                            if (another.xx1 <= xx2) {
                                result = xx1;
                            }
                        } else {
                            if (xx1 <= another.xx2) {
                                result = another.xx1;
                            }
                        }
                    }
                    break;
            }
        }
        return result;
    }

    /**
     *
     * toa do yy cua dau start
     */
    public int overlapSameDirectionYy1(ConveyerBelt another) {
        int result = -1;
        if (direction == another.direction) {
            switch (direction) {
                case 2:
                    if (xx1 == another.xx1) {
                        if (yy1 <= another.yy1) {
                            if (another.yy1 <= yy2) {
                                result = yy1;
                            }
                        } else {
                            if (yy1 <= another.yy2) {
                                result = another.yy1;
                            }
                        }
                    }
                    break;

                case 3:
                    if (yy1 == another.yy1) {
                        if (xx1 >= another.xx1) {
                            if (another.xx1 >= xx2) {
                                result = yy1;
                            }
                        } else {
                            if (xx1 >= another.xx2) {
                                result = yy1;
                            }
                        }
                    }
                    break;

                case 4:
                    if (xx1 == another.xx1) {
                        if (yy1 >= another.yy1) {
                            if (another.yy1 >= yy2) {
                                result = yy1;
                            }
                        } else {
                            if (yy1 >= another.yy2) {
                                result = another.yy1;
                            }
                        }
                    }
                    break;

                case 1:
                    if (yy1 == another.yy1) {
                        if (xx1 <= another.xx1) {
                            if (another.xx1 <= xx2) {
                                result = yy1;
                            }
                        } else {
                            if (xx1 <= another.xx2) {
                                result = yy1;
                            }
                        }
                    }
                    break;
            }
        }
        return result;
    }

    /**
     *
     * toa do xx cua dau end
     */
    public int overlapSameDirectionXx2(ConveyerBelt another) {
        int result = -1;
        if (direction == another.direction) {
            switch (direction) {
                case 2:
                    if (xx1 == another.xx1) {
                        if (yy1 <= another.yy1) {
                            if (another.yy1 <= yy2) {
                                result = xx2;
                            }
                        } else {
                            if (yy1 <= another.yy2) {
                                result = xx2;
                            }
                        }
                    }
                    break;

                case 3:
                    if (yy1 == another.yy1) {
                        if (xx1 >= another.xx1) {
                            if (another.xx1 >= xx2) {
                                if (xx2 <= another.xx2) {
                                    result = xx2;
                                } else {
                                    result = another.xx2;
                                }
                            }
                        } else {
                            if (xx1 >= another.xx2) {
                                if (xx2 <= another.xx2) {
                                    result = xx2;
                                } else {
                                    result = another.xx2;
                                }
                            }
                        }
                    }
                    break;

                case 4:
                    if (xx1 == another.xx1) {
                        if (yy1 >= another.yy1) {
                            if (another.yy1 >= yy2) {
                                result = xx2;
                            }
                        } else {
                            if (yy1 >= another.yy2) {
                                result = xx2;
                            }
                        }
                    }
                    break;

                case 1:
                    if (yy1 == another.yy1) {
                        if (xx1 <= another.xx1) {
                            if (another.xx1 <= xx2) {
                                if (xx2 >= another.xx2) {
                                    result = xx2;
                                } else {
                                    result = another.xx2;
                                }
                            }
                        } else {
                            if (xx1 <= another.xx2) {
                                if (xx2 >= another.xx2) {
                                    result = xx2;
                                } else {
                                    result = another.xx2;
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return result;
    }

    /**
     *
     * toa do yy cua dau end
     */
    public int overlapSameDirectionYy2(ConveyerBelt another) {
        int result = -1;
        if (direction == another.direction) {
            switch (direction) {
                case 2:
                    if (xx1 == another.xx1) {
                        if (yy1 <= another.yy1) {
                            if (another.yy1 <= yy2) {
                                if (yy2 >= another.yy2) {
                                    result = yy2;
                                } else {
                                    result = another.yy2;
                                }
                            }
                        } else {
                            if (yy1 <= another.yy2) {
                                if (yy2 >= another.yy2) {
                                    result = yy2;
                                } else {
                                    result = another.yy2;
                                }
                            }
                        }
                    }
                    break;

                case 3:
                    if (yy1 == another.yy1) {
                        if (xx1 >= another.xx1) {
                            if (another.xx1 >= xx2) {
                                result = yy2;
                            }
                        } else {
                            if (xx1 >= another.xx2) {
                                result = yy2;
                            }
                        }
                    }
                    break;

                case 4:
                    if (xx1 == another.xx1) {
                        if (yy1 >= another.yy1) {
                            if (another.yy1 >= yy2) {
                                if (yy2 <= another.yy2) {
                                    result = yy2;
                                } else {
                                    result = another.yy2;
                                }
                            }
                        } else {
                            if (yy1 >= another.yy2) {
                                if (another.yy1 >= yy2) {
                                    if (yy2 <= another.yy2) {
                                        result = yy2;
                                    } else {
                                        result = another.yy2;
                                    }
                                }
                            }
                        }
                    }
                    break;

                case 1:
                    if (yy1 == another.yy1) {
                        if (xx1 <= another.xx1) {
                            if (another.xx1 <= xx2) {
                                result = yy2;
                            }
                        } else {
                            if (xx1 <= another.xx2) {
                                result = yy2;
                            }
                        }
                    }
                    break;
            }
        }
        return result;
    }

    /**
     *
     * tra lai kieu chong nhau nguoc chieu
     * 0: ko chong nhau
     * 1: trung start - legal -> diem giao se la arrow 2 chieu ve 2 phia
     * 2: trung end - legal -> diem giao se la arrow 0 chieu
     * 3: nam trong nhau, chong nhau o end nhieu hon 1 cell, chong nhau o start nhieu hon 1 cell - illegal
     */
    public int overlapOppositeDirection(ConveyerBelt another) {
        if ((direction != another.direction) && (((direction + another.direction) == 4) || ((direction + another.direction) == 6))) {
            if ((direction % 2 == 0) && (xx1 == another.xx1)) {
                if ((xx1 == another.xx1) && (yy1 == another.yy1)) {
                    return 1;
                }
                if ((xx2 == another.xx2) && (yy2 == another.yy2)) {
                    return 2;
                }
                // end thang nay nam trong thang kia
                if (((yy2 - another.yy1) * (yy2 - another.yy2) <= 0) || ((another.yy2 - yy1) * (another.yy2 - yy2) <= 0)) {
                    return 3;
                }
                // start thang nay nam trong thang kia
                if (((yy1 - another.yy1) * (yy1 - another.yy2) <= 0) || ((another.yy1 - yy1) * (another.yy1 - yy2) <= 0)) {
                    return 3;
                }
            }
            if ((direction % 2 == 1) && (yy1 == another.yy1)) {
                if ((xx1 == another.xx1) && (yy1 == another.yy1)) {
                    return 1;
                }
                if ((xx2 == another.xx2) && (yy2 == another.yy2)) {
                    return 2;
                }
                // end thang nay nam trong thang kia
                if (((xx2 - another.xx1) * (xx2 - another.xx2) <= 0) || ((another.xx2 - xx1) * (another.xx2 - xx2) <= 0)) {
                    return 3;
                }
                // start thang nay nam trong thang kia
                if (((xx1 - another.xx1) * (xx1 - another.xx2) <= 0) || ((another.xx1 - xx1) * (another.xx1 - xx2) <= 0)) {
                    return 3;
                }
            }
        }
        return 0;
    }

    /**
     * xx cua diem start
     */
    public int overlapOppositeDirectionStartXx(ConveyerBelt another) {
        int result = -1;
        if ((direction != another.direction) && (((direction + another.direction) == 4) || ((direction + another.direction) == 6))) {
            if ((direction % 2 == 0) && (xx1 == another.xx1)) {
                if ((xx1 == another.xx1) && (yy1 == another.yy1)) {
                    result = xx1;
                }
            }
            if ((direction % 2 == 1) && (yy1 == another.yy1)) {
                if ((xx1 == another.xx1) && (yy1 == another.yy1)) {
                    result = xx1;
                }
            }
        }
        return result;
    }

    /**
     * yy cua diem start
     */
    public int overlapOppositeDirectionStartYy(ConveyerBelt another) {
        int result = -1;
        if ((direction != another.direction) && (((direction + another.direction) == 4) || ((direction + another.direction) == 6))) {
            if ((direction % 2 == 0) && (xx1 == another.xx1)) {
                if ((xx1 == another.xx1) && (yy1 == another.yy1)) {
                    result = yy1;
                }
            }
            if ((direction % 2 == 1) && (yy1 == another.yy1)) {
                if ((xx1 == another.xx1) && (yy1 == another.yy1)) {
                    result = yy1;
                }
            }
        }
        return result;
    }

    /**
     * xx cua diem end
     */
    public int overlapOppositeDirectionEndXx(ConveyerBelt another) {
        int result = -1;
        if ((direction != another.direction) && (((direction + another.direction) == 4) || ((direction + another.direction) == 6))) {
            if ((direction % 2 == 0) && (xx1 == another.xx1)) {
                if ((xx2 == another.xx2) && (yy2 == another.yy2)) {
                    result = xx2;
                }
            }
            if ((direction % 2 == 1) && (yy1 == another.yy1)) {
                if ((xx2 == another.xx2) && (yy2 == another.yy2)) {
                    result = xx2;
                }
            }
        }
        return result;
    }

    /**
     * xx cua diem start
     */
    public int overlapOppositeDirectionEndYy(ConveyerBelt another) {
        int result = -1;
        if ((direction != another.direction) && (((direction + another.direction) == 4) || ((direction + another.direction) == 6))) {
            if ((direction % 2 == 0) && (xx1 == another.xx1)) {
                if ((xx2 == another.xx2) && (yy2 == another.yy2)) {
                    result = yy2;
                }
            }
            if ((direction % 2 == 1) && (yy1 == another.yy1)) {
                if ((xx2 == another.xx2) && (yy2 == another.yy2)) {
                    result = yy2;
                }
            }
        }
        return result;
    }
}
