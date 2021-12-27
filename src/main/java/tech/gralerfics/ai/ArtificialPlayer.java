package tech.gralerfics.ai;

import java.util.ArrayList;

public class ArtificialPlayer {
    private static final int [][]w = {{500,-25,10,5,5,10,-25,500},{-25,-45,1,1,1,1,-45,-25},{10,1,3,2,2,3,1,10},{5,1,2,1,1,2,1,5},{5,1,2,1,1,2,1,5},{10,1,3,2,2,3,1,10},{-25,-45,1,1,1,1,-45,-25},{500,-25,10,5,5,10,-25,500}};
    private static final int inf = 1000000007;
    private int [][] mp = new int[8][8];
    private int my_color,opp_color;
    private int max_depth = 4;
    private Move Final = null; // new Move();
    public boolean isCheat;
//    public boolean interrupt = false;

    public int getMax_depth() {
        return max_depth;
    }

    public void setMax_depth(int max_depth) {
        this.max_depth=max_depth;
    }

    private int mapWeightSum(int color) {
        int res = 0;
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                if (mp[i][j]==color) {
                    res+=w[i][j];
                } else if (mp[i][j]==-color) {
                    res-=w[i][j];
                }
        return res;
    }

    private ArrayList<Move>possiMove = new ArrayList<Move>();

    private int mapMoves(int color) {
        possiMove.clear();
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++) if (mp[i][j]==0) {
                int p; boolean flag=false;
                if (isCheat) {
                    flag = true;
                } else {
                    p=j+1; while (p<8&&mp[i][p]==-color) p++;
                    if (p<8&&mp[i][p]==color&&p!=j+1) flag=true;
                    p=j-1; while (p>=0&&mp[i][p]==-color) p--;
                    if (p>=0&&mp[i][p]==color&&p!=j-1) flag=true;
                    p=i+1; while (p<8&&mp[p][j]==-color) p++;
                    if (p<8&&mp[p][j]==color&&p!=i+1) flag=true;
                    p=i-1; while (p>=0&&mp[p][j]==-color) p--;
                    if (p>=0&&mp[p][j]==color&&p!=i-1) flag=true;
                    p=1; while (i+p<8&&j+p<8&&mp[i+p][j+p]==-color) p++;
                    if (i+p<8&&j+p<8&&mp[i+p][j+p]==color&&p!=1) flag=true;
                    p=1; while (i-p>=0&&j-p>=0&&mp[i-p][j-p]==-color) p++;
                    if (i-p>=0&&j-p>=0&&mp[i-p][j-p]==color&&p!=1) flag=true;
                    p=1; while (i+p<8&&j-p>=0&&mp[i+p][j-p]==-color) p++;
                    if (i+p<8&&j-p>=0&&mp[i+p][j-p]==color&&p!=1) flag=true;
                    p=1; while (i-p>=0&&j+p<8&&mp[i-p][j+p]==-color) p++;
                    if (i-p>=0&&j+p<8&&mp[i-p][j+p]==color&&p!=1) flag=true;
                }
                if (flag) possiMove.add(new Move(i,j));
            }
        return possiMove.size();
    }

    private void draw(int i,int j,int color) {
        int p;
        p=j+1; while (p<8&&mp[i][p]==-color) p++;
        if (p<8&&mp[i][p]==color&&p!=j+1) {
            p=j+1; while (p<8&&mp[i][p]==-color) mp[i][p++]=color;
        }
        p=j-1; while (p>=0&&mp[i][p]==-color) p--;
        if (p>=0&&mp[i][p]==color&&p!=j-1) {
            p=j-1; while (p>=0&&mp[i][p]==-color) mp[i][p--]=color;
        }
        p=i+1; while (p<8&&mp[p][j]==-color) p++;
        if (p<8&&mp[p][j]==color&&p!=i+1) {
            p=i+1; while (p<8&&mp[p][j]==-color) mp[p++][j]=color;
        }
        p=i-1; while (p>=0&&mp[p][j]==-color) p--;
        if (p>=0&&mp[p][j]==color&&p!=i-1) {
            p=i-1; while (p>=0&&mp[p][j]==-color) mp[p--][j]=color;
        }
        p=1; while (i+p<8&&j+p<8&&mp[i+p][j+p]==-color) p++;
        if (i+p<8&&j+p<8&&mp[i+p][j+p]==color&&p!=1) {
            p=1; while (i+p<8&&j+p<8&&mp[i+p][j+p]==-color) {
                mp[i+p][j+p]=color; p++;
            }
        }
        p=1; while (i-p>=0&&j-p>=0&&mp[i-p][j-p]==-color) p++;
        if (i-p>=0&&j-p>=0&&mp[i-p][j-p]==color&&p!=1) {
            p=1; while (i-p>=0&&j-p>=0&&mp[i-p][j-p]==-color) {
                mp[i-p][j-p]=color; p++;
            }
        }
        p=1; while (i+p<8&&j-p>=0&&mp[i+p][j-p]==-color) p++;
        if (i+p<8&&j-p>=0&&mp[i+p][j-p]==color&&p!=1) {
            p=1; while (i+p<8&&j-p>=0&&mp[i+p][j-p]==-color) {
                mp[i+p][j-p]=color; p++;
            }
        }
        p=1; while (i-p>=0&&j+p<8&&mp[i-p][j+p]==-color) p++;
        if (i-p>=0&&j+p<8&&mp[i-p][j+p]==color&&p!=1) {
            p=1; while (i-p>=0&&j+p<8&&mp[i-p][j+p]==-color) {
                mp[i-p][j+p]=color; p++;
            }
        }
        mp[i][j]=color;
    }

    private int mapStables(int color) {
        int [] cind1={0,0,7,7};
        int [] cind2={0,7,7,0};
        int [] inc1={0,1,0,-1};
        int [] inc2={1,0,-1,0};
        int [] stop={0,0,0,0};
        int res = 0;
        boolean [][] stable = new boolean [8][8];
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                stable[i][j] = false;
        for (int i=0;i<4;i++) if (mp[cind1[i]][cind2[i]]==color) {
            stable[cind1[i]][cind2[i]]=true; stop[i]=1; res++;
            for (int j=1;j<7;j++)
                if (mp[cind1[i]+inc1[i]*j][cind2[i]+inc2[i]*j] != color) {
                    break;
                } else {
                    stable[cind1[i]+inc1[i]*j][cind2[i]+inc2[i]*j]=true; stop[i]=j+1;
                    res++;
                }
        }
        for (int i=0;i<4;i++) if (mp[cind1[i]][cind2[i]]==color) {
            for (int j=1;j<7-((i==0)?0:stop[i-1]);j++) {
                if (mp[cind1[i]-((i==0)?0:inc1[i-1])*j][cind2[i]-(i==0?0:inc2[i-1])*j] != color) {
                    break;
                } else {
                    stable[cind1[i]-((i==0)?0:inc1[i-1])*j][cind2[i]-((i==0)?0:inc2[i-1])*j]=true;
                    res++;
                }
            }
        }
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                if (!stable[i][j]&&mp[i][j]==color) {
                    boolean flag=true;
                    for (int k=0;k<8;k++) if (mp[i][k]==0||mp[k][j]==0) {
                        flag=false; break;
                    }
                    if (!flag) continue;
                    int p;
                    p=1; while (i+p<8&&j+p<8&&mp[i+p][j+p]!=0) p++;
                    if (!(i+p>=8||j+p>=8)) continue;
                    p=1; while (i-p>=0&&j-p>=0&&mp[i-p][j-p]!=0) p++;
                    if (!(i-p<0||j-p<0)) continue;
                    p=1; while (i+p<8&&j-p>=0&&mp[i+p][j-p]!=0) p++;
                    if (!(i+p>=8||j-p<0)) continue;
                    p=1; while (i-p>=0&&j+p<8&&mp[i-p][j+p]!=0) p++;
                    if (!(i-p<0||j+p>=8)) continue;
                    res++;
                }
        return res;
    }

    private int getValue() {
        return mapWeightSum(my_color)+15*(mapMoves(my_color)-mapMoves(opp_color))+10*mapStables(my_color);
    }

    private int sign(int x) {
        return (x == my_color)?1:-1;
    }

    private int search(int a,int b,int color,int depth) {
//        if (interrupt && Final != null) {
//            return 0;
//        }

        int Max = -inf;
        if (depth<=0) return sign(color)*getValue();
        if (mapMoves(color)==0) {
            if (mapMoves(-color)==0)
                return sign(color)*getValue();
            return -search(-b,-a,-color,depth);
        }

        ArrayList<Move>allMoves = new ArrayList<Move>();

        for (Move move :  possiMove) {
            allMoves.add(move);
        }
        for (Move move : allMoves) {
            int [][]rec = new int [8][8];
            for (int i=0;i<8;i++)
                for (int j=0;j<8;j++)
                    rec[i][j] = mp[i][j];
            draw(move.getX(),move.getY(),color);
            int val = -search(-b,-a,-color,depth-1);
            for (int i=0;i<8;i++)
                for (int j=0;j<8;j++)
                    mp[i][j] = rec[i][j];
            if (val > a) {
                if (val >= b) return val;
                a = Math.max(val,a);
            }
            if (val > Max) {
                Max = val;
                if (depth == max_depth) {
                    Final = move;
                }
            }
        }
        return Max;
    }

    public void init(int [][] mp, int cur, int depth, boolean cheating) {
        this.max_depth = depth;
        my_color = cur;
        opp_color = - cur;
        isCheat = cheating;
//        interrupt = false;
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                this.mp[i][j] = mp[i][j];
    }

    public int process() {
//        AIInterruptThread atr = new AIInterruptThread(this);
//        atr.needed = true;
//        atr.start();
        search(-inf, inf, my_color, max_depth);
//        atr.needed = false;
        return Final.getNumber();
    }
}
