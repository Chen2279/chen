package cn.ch.util;

import cn.ch.model.Label;

public class FHeap implements Heap{

    private int keyNum;         // ���нڵ������
    private FibNode min;        // ��С�ڵ�(ĳ����С�ѵĸ��ڵ�)

    
    /*
     * Construction
     */
    public FHeap() {
        this.keyNum = 0;
        this.min = null;
    }
    
    /* function in interface Heap
     * ��Label���뵽쳲���������
     */
    public void Insert(Label label) {
        FibNode node;

        node = new FibNode(label);
        if (node == null)
            return ;

        insert(node);
        label.Status(true);
    }
    
    public Label ExtractMin() {
    	
    	if(this.keyNum > 0){
    		FibNode node = this.DeleteMin();
    		Label label = node.label;
    		node.label.Status(false);
    		node.ReleaseLabel();
    		return label;
    	}
    	return null;
    }
    
    public void Delete(Label label) {
        if (min==null)
            return ;

        //FibNode node = search(key);
//        if (node==null)
//            return ;
        if(label.Status()){
        	this.Delete(label.getQueueNode());
        	label.Status(false);
        	label.getQueueNode().ReleaseLabel();
        }
        
    }
    
    ////////////////////////////////////////////////////////////////////////
    
    /*
     * ��ȡ쳲�����������СLabel��ʧ�ܷ���null
     */
    public Label FindMin() {
        if (min==null)
            return null;

        return min.label;
    }
    
    /*
     * ���ڵ�node���뵽쳲���������
     */
    private void insert(FibNode node) {
        if (keyNum == 0)
            min = node;
        else {
            addNode(node, min);
            if (node.key < min.key)
                min = node;
        }

        keyNum++;
    }

    /* 
     * ��node��˫�����Ƴ�
     */
    private void removeNode(FibNode node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }
     
    /*
     * ��node�ѽ�����root���֮ǰ(ѭ��������)
     *   a ���� root
     *   a ���� node ���� root
    */
    private void addNode(FibNode node, FibNode root) {
    	try{
        node.left        = root.left;
        root.left.right  = node;
        node.right       = root;
        root.left        = node;
    	}catch(Exception e){
    		this.print();
    	}
    }
     
    /*
     * �Ƴ���С�ڵ�
     */
    public FibNode DeleteMin() {
        if (min==null)
            return null;

        FibNode m = min;
        // ��minÿһ������(���ӺͶ��ӵ��ֵ�)����ӵ�"쳲������ѵĸ�����"��
        while (m.child != null) {
            FibNode child = m.child;

            removeNode(child);
            if (child.right == child)
                m.child = null;
            else
                m.child = child.right;

            addNode(child, min);
            child.parent = null;
        }

        // ��m�Ӹ��������Ƴ�
        removeNode(m);
        keyNum--;
        // ��m�Ƕ���Ψһ�ڵ㣬�����öѵ���С�ڵ�Ϊnull��
        // �������öѵ���С�ڵ�Ϊһ���ǿսڵ�(m.right)��Ȼ���ٽ��е��ڡ�
        if(keyNum == 506 && m.label.getAssociated().vertexNum == 9728 && m.key >= 263.8 && m.key <= 263.8911){
        	int a = 0;
        }
        if (m.right == m)
            min = null;
        else {
            min = m.right;
            consolidate();
        }
        if(min == null){
        	int a = 0;
        }
        if (m.right == m){
        	int a = 0;
        }
        return m;
    }
     
    /* 
     * �ϲ�쳲������ѵĸ�������������ͬ��������
     */
    private void consolidate() {
        // ����log2(keyNum)��floor��ζ������ȡ����
        // ex. log2(13) = 3������ȡ��Ϊ4��
        int maxDegree = (int) Math.floor(Math.log(keyNum) / Math.log(2.0));
        int D = maxDegree + 1;
        FibNode[] cons = new FibNode[D+1];

        for (int i = 0; i <= D; i++)
            cons[i] = null;
     
        // �ϲ���ͬ�ȵĸ��ڵ㣬ʹÿ����������Ψһ
        while (min != null) {
            FibNode x = getNextSubTree();            // ȡ�����е���С��(��С�ڵ����ڵ���)
            int d = x.degree;                        // ��ȡ��С���Ķ���
            // cons[d] != null����ζ����������(x��y)��"����"��ͬ,�ϲ�����������
            while (cons[d] != null) {
                FibNode y = cons[d];                // y��"��x�Ķ�����ͬ����" 
                if (x.key > y.key) {    // ��֤x�ļ�ֵ��yС
                    FibNode tmp = x;
                    x = y;
                    y = tmp;
                }

                link(y, x);    // ��y���ӵ�x��
                cons[d] = null;
                d++;
            }
            cons[d] = x;
        }
        min = null;
     
        // ��cons�еĽ�����¼ӵ�������
        for (int i=0; i<=D; i++) {

            if (cons[i] != null) {
                if (min == null)
                    min = cons[i];
                else {
                    addNode(cons[i], min);
                    if ((cons[i]).key < min.key)
                        min = cons[i];
                }
            }
        }
    }
    
    /*
     * ��minָ��min����һ���ֵܽڵ㣬
     * ��minָ������������
     */
    public FibNode getNextSubTree() {
        FibNode p = min;

        if (p == p.right)
            min = null;
        else {
            removeNode(p);
            min = p.right;
        }
        p.left = p.right = p;

        return p;
    }
     
    /*
     * ��node���ӵ�root�����
     */
    private void link(FibNode node, FibNode root) {
        // ��node��˫�������Ƴ�
        removeNode(node);
        // ��node��Ϊroot�ĺ���
        if (root.child == null)
            root.child = node;
        else
            addNode(node, root.child);

        node.parent = root;
        root.degree++;
        node.marked = false;
    }
    
    /* 
     * ��쳲��������нڵ�node��ֵ����Ϊkey
     */
    private void DecreaseCost(FibNode node, double key) {
        if (min==null ||node==null) 
            return ;

        if (key > node.key) {
            System.out.printf("decrease failed: the new key(%d) is no smaller than current key(%d)\n", key, node.key);
            return ;
        }

        FibNode parent = node.parent;
        node.key = key;
        if (parent!=null && (node.key < parent.key)) {
            // ��node�Ӹ��ڵ�parent�а������������node��ӵ���������
            cut(node, parent);
            cascadingCut(parent);
        }

        // ������С�ڵ�
        if (node.key < min.key)
            min = node;
    }
     
    /* 
     * ��node�Ӹ��ڵ�parent���������а��������
     * ��ʹnode��Ϊ"�ѵĸ�����"�е�һԱ��
     */
    private void cut(FibNode node, FibNode parent) {
        removeNode(node);
        //renewDegree(parent, node.degree);
        parent.degree--;
        // nodeû���ֵ�
        if (node == node.right) 
            parent.child = null;
        else 
            parent.child = node.right;

        node.parent = null;
        node.left = node.right = node;
        node.marked = false;
        // ��"node������"��ӵ�"������"��
        addNode(node, min);
    }
     
    /* 
     * �Խڵ�node����"��������"
     *
     * �������У������С��Ľ���ƻ�����С�����ʣ�
     *     �����������(��������˫��������ɾ��������
     *     ����뵽����С�����ڵ��γɵ�˫��������)��
     *     Ȼ���ٴ�"���нڵ�ĸ��ڵ�"�����������ڵ�ݹ�ִ�м�����֦
     */
    private void cascadingCut(FibNode node) {
        FibNode parent = node.parent;

        if (parent != null) {
            if (node.marked == false) 
                node.marked = true;
            else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }
    
    /*
     * ɾ�����node
     */
    private void Delete(FibNode node) {
    	if(node == null){
    		return;
    	}
    	if(node == min){
    		DeleteMin();
    		return;
    	}
    	
    	removeNode(node);
    	
    	
        double m = min.key;
        DecreaseCost(node, m-1);
        DeleteMin();
    }
    
    /////////////////////////////////////////////////////////////
     
    /*
     * ��˫������b���ӵ�˫������a�ĺ���
     */
    private void catList(FibNode a, FibNode b) {
        FibNode tmp;

        tmp           = a.right;
        a.right       = b.right;
        b.right.left  = a;
        b.right       = tmp;
        tmp.left      = b;
    }

    /*
     * ��other�ϲ�����ǰ����
     */
    public void union(FHeap other) {
        if (other==null)
            return ;

        if((this.min) == null) {                // this��"��С�ڵ�"
            this.min = other.min;
            this.keyNum = other.keyNum;
            other = null;
        } else if((other.min) == null) {        // this��"��С�ڵ�" && other��"��С�ڵ�"
            other = null;
        } else {                                // this��"��С�ڵ�" && other��"��С�ڵ�"
            // ��"other�и�����"��ӵ�"this"��
            catList(this.min, other.min) ;

            if (this.min.key > other.min.key)
                this.min = other.min;
            this.keyNum += other.keyNum;
            other = null;;
        }
    }

    
      
    /* 
     * �޸Ķ���
     */
    private void renewDegree(FibNode parent, int degree) {
        parent.degree -= degree;
        if (parent. parent != null)
            renewDegree(parent.parent, degree);
    }
     
    

    

    

    /* 
     * ��쳲��������нڵ�node��ֵ����Ϊkey
     */
    private void increase(FibNode node, int key) {
        if (min==null ||node==null) 
            return ;

        if ( key <= node.key) {
            System.out.printf("increase failed: the new key(%d) is no greater than current key(%d)\n", key, node.key);
            return ;
        }

        // ��nodeÿһ������(����������,����,...)����ӵ�"쳲������ѵĸ�����"��
        while (node.child != null) {
            FibNode child = node.child;
            removeNode(child);               // ��child��node����������ɾ��
            if (child.right == child)
                node.child = null;
            else
                node.child = child.right;

            addNode(child, min);       // ��child��ӵ���������
            child.parent = null;
        }
        node.degree = 0;
        node.key = key;

        // ���node���ڸ������У�
        //     ��node�Ӹ��ڵ�parent���������а��������
        //     ��ʹnode��Ϊ"�ѵĸ�����"�е�һԱ��
        //     Ȼ�����"��������"
        // �������ж��Ƿ���Ҫ���¶ѵ���С�ڵ�
        FibNode parent = node.parent;
        if(parent != null) {
            cut(node, parent);
            cascadingCut(parent);
        } else if(min == node) {
            FibNode right = node.right;
            while(right != node) {
                if(node.key > right.key)
                    min = right;
                right = right.right;
            }
        }
    }

    /* 
     * ����쳲������ѵĽڵ�node�ļ�ֵΪkey
     */
    private void update(FibNode node, int key) {
        if(key < node.key)
        	DecreaseCost(node, key);
        else if(key > node.key)
            increase(node, key);
        else
            System.out.printf("No need to update!!!\n");
    }
      
    public void update(int oldkey, int newkey) {
        FibNode node;

        node = search(oldkey);
        if (node!=null)
            update(node, newkey);
    }

    /*
     * ����С��root�в��Ҽ�ֵΪkey�Ľڵ�
     */
    private FibNode search(FibNode root, int key) {
        FibNode t = root;    // ��ʱ�ڵ�
        FibNode p = null;    // Ҫ���ҵĽڵ�

        if (root==null)
            return root;

        do {
            if (t.key == key) {
                p = t;
                break;
            } else {
                if ((p = search(t.child, key)) != null) 
                    break;
            }
            t = t.right;
        } while (t != root);

        return p;
    }
     
    /*
     * ��쳲��������в��Ҽ�ֵΪkey�Ľڵ�
     */
    private FibNode search(int key) {
        if (min==null)
            return null;

        return search(min, key);
    }

    /*
     * ��쳲����������Ƿ���ڼ�ֵΪkey�Ľڵ㡣
     * ���ڷ���true�����򷵻�false��
     */
    public boolean contains(int key) {
        return search(key)!=null ? true: false;
    }

    

    
     
    /* 
     * ����쳲�������
     */
    private void destroyNode(FibNode node) {
        if(node == null)
            return;

        FibNode start = node;
        do {
            destroyNode(node.child);
            // ����node������nodeָ����һ��
            node = node.right;
            node.left = null;
        } while(node != start);
    }
     
    public void destroy() {
        destroyNode(min);
    }

    /*
     * ��ӡ"쳲�������"
     *
     * ����˵����
     *     node       -- ��ǰ�ڵ�
     *     prev       -- ��ǰ�ڵ��ǰһ���ڵ�(���ڵ�or�ֵܽڵ�)
     *     direction  --  1����ʾ��ǰ�ڵ���һ������;
     *                    2����ʾ��ǰ�ڵ���һ���ֵܽڵ㡣
     */
    private void print(FibNode node, FibNode prev, int direction) {
        FibNode start=node;

        if (node==null)
            return ;
        do {
            if (direction == 1)
                System.out.printf("%8d(%d) is %2d's child\n", node.key, node.degree, prev.key);
            else
                System.out.printf("%8d(%d) is %2d's next\n", node.key, node.degree, prev.key);

            if (node.child != null)
                print(node.child, node, 1);

            // �ֵܽڵ�
            prev = node;
            node = node.right;
            direction = 2;
        } while(node != start);
    }

    public void print() {
        if (min==null){
        	System.out.printf("== min null: ==\n");
            return ;
        }
        	

        int i=0;
        FibNode p = min;
        System.out.printf("== 쳲������ѵ���ϸ��Ϣ: ==\n");
        do {
            i++;
            System.out.printf("%2d. %4d(%d) is root\n", i, p.key, p.degree);

            print(p.child, p, 1);
            p = p.right;
        } while (p != min);
        System.out.printf("\n");
    }
    
   

	@Override
	public int size() {
		// TODO �Զ����ɵķ������
		return this.keyNum;
	}
	
	 public class FibNode {
	        double key;            // �ؼ���(��ֵ)
	        Label label;
	        int degree;            // ����
	        FibNode left;        // ���ֵ�
	        FibNode right;        // ���ֵ�
	        FibNode child;        // ��һ�����ӽڵ�
	        FibNode parent;        // ���ڵ�
	        boolean marked;     // �Ƿ�ɾ����һ������

	        public FibNode(Label label){
	        	if(label == null){
	        		try {
						throw new Exception("The label is null");
					} catch (Exception e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
	        	}
	            this.key    = label.getCost();
	            this.label = label;
	            this.degree = 0;
	            this.marked = false;
	            this.left   = this;
	            this.right  = this;
	            this.parent = null;
	            this.child  = null;
	            
	            this.label.setQueueNode(this);
	        }
	        
	        public void ReleaseLabel(){
	        	this.label.setQueueNode(null);
	        	this.label = null;
	        }
	    }
}


