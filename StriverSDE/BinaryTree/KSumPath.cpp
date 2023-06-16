void check(Node *root, int k, int &count, int sum, unordered_map<int, int> &mp) {
        if(root == NULL) 
            return;
        
        sum += root->data;

        count += mp[sum-k];
        if(sum == k)
            count++;
            
        mp[sum]++;
        
        check(root->left, k, count, sum, mp);
        check(root->right, k, count, sum, mp);
        
        mp[sum]--;
    }
    int sumK(Node *root,int k)
    {
         int count = 0;
        unordered_map<int, int> mp;
        check(root, k, count, 0, mp);
        return count;
    }
