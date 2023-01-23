int main() {
    int floor ;
    cin >> floor ;
    int max = 0 ;
    for (int i = 0 ; i < floor ; i++) {
        int n ;
        cin >> n ;
        for(int j = 0 ; j < floor ; j++ ) {
            int gold ;
            cin >> gold ;
            if (gold > max ) {
                max = gold ;
            }
        }
    }
    cout << max + floor ;
    return 0 ;
}
