// you can use includes, for example:
// #include <algorithm>

// you can write to stdout for debugging purposes, e.g.
// cout << "this is a debug message" << endl;
vector<vector<int>> dp;

int recur(vector<int>& h,int pos,int r_x, int r_y,int n){    //r_x: remaining time in x r_y: remaining time 
    if(pos>=n) return 0;
    if(dp[pos][abs(r_x-r_y)]!=-1) return dp[pos][abs(r_x-r_y)];
    int v1=0,v2=0,v3=0;
    if(r_x>=h[pos]){
        v1=1+recur(h,pos+1,r_x-h[pos],r_y,n);
    }
    if(r_y>=h[pos]){
        v2=1+recur(h,pos+1,r_x,r_y-h[pos],n);
    }
    v3=recur(h,pos+1,r_x,r_y,n);
    return dp[pos][abs(r_x-r_y)]=max(v1,max(v2,v3));
}

int solution(vector<int> &H, int X, int Y) {
    int N = H.size();
    dp.clear();
    dp.resize(N, vector<int>(2 * (X + Y) + 1, -1));
    return recur(H, 0, X, Y, N); 
}


