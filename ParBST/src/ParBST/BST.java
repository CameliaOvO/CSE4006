package ParBST;

/**
 * *************************************************************************
 * The sequential Binary Search Tree (for storing int values)
 *****************************************************************************/

import java.util.concurrent.locks.ReentrantLock;

public class BST {

    private Node root;

    private ReentrantLock treeLock;

    public BST() {
        root = null;
        treeLock = new ReentrantLock();
    }

    /*****************************************************
     *
     *            INSERT
     *
     ******************************************************/
    public boolean insert(int data) {
        treeLock.lock();

        if (root == null) {
            root = new Node(data);
            treeLock.unlock();
        } else {
            Node currNode = root;
            Node prevNode;
            currNode.nodeLock.lock();
            treeLock.unlock();

            while (true) {
                if (data == currNode.data) {
                    currNode.nodeLock.unlock();
                    return false;
                }

                if (data < currNode.data) {
                    if (currNode.left == null) {
                        currNode.left = new Node(data);
                        currNode.nodeLock.unlock();
                        return true;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.left;
                        currNode.nodeLock.lock();
                        prevNode.nodeLock.unlock();

                    }
                } else {
                    if (currNode.right == null) {
                        currNode.right = new Node(data);
                        currNode.nodeLock.unlock();
                        return true;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.right;
                        currNode.nodeLock.lock();
                        prevNode.nodeLock.unlock();
                    }
                }
            }
        }
        return true;
    }


    /*****************************************************
     *
     *            SEARCH
     *
     ******************************************************/


    public boolean search(int toSearch) {
        treeLock.lock();

        if (root == null) {
            treeLock.unlock();
            return false;
        } else {
            Node currNode = root;
            Node prevNode;
            currNode.nodeLock.lock();
            treeLock.unlock();

            while (true) {
                if (toSearch == currNode.data) {
                    currNode.nodeLock.unlock();
                    return true;
                }

                if (toSearch < currNode.data) {
                    if (currNode.left == null) {
                        currNode.nodeLock.unlock();
                        return false;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.left;
                        currNode.nodeLock.lock();
                        prevNode.nodeLock.unlock();

                    }
                } else {
                    if (currNode.right == null) {
                        currNode.nodeLock.unlock();
                        return false;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.right;
                        currNode.nodeLock.lock();
                        prevNode.nodeLock.unlock();
                    }
                }
            }
        }

    }

    /*****************************************************
     *
     *            DELETE
     *
     ******************************************************/


    public boolean delete(int toDelete) {
        treeLock.lock();

        if (root == null) {
            treeLock.unlock();
            return false;
        } else {
            Node currNode = root;
            Node prevNode;
            currNode.nodeLock.lock();

            if (currNode.data != toDelete) {
                prevNode = currNode;
                if (currNode.data > toDelete) {
                    currNode = currNode.left;
                } else {
                    currNode = currNode.right;
                }
                currNode.nodeLock.lock();
                treeLock.unlock();

                while (true) {
                    if (currNode.data == toDelete) {
                        Node replaced = replaceNode(currNode);

                        if (prevNode.data > toDelete) {
                            prevNode.left = replaced;
                        } else {
                            prevNode.right = replaced;
                        }

                        if (replaced != null) {
                            replaced.left = currNode.left;
                            replaced.right = currNode.right;
                        }

                        currNode.nodeLock.unlock();
                        prevNode.nodeLock.unlock();

                        break;

                    } else {
                        prevNode.nodeLock.unlock();
                        prevNode = currNode;

                        if (currNode.data > toDelete) {
                            currNode = currNode.left;
                        } else {
                            currNode = currNode.right;
                        }
                    }

                    if (currNode == null) {
                        return false;
                    } else {
                        currNode.nodeLock.lock();
                    }
                }
            } else {
                Node replaced = replaceNode(currNode);
                root = replaced;

                if (replaced != null) {
                    replaced.left = currNode.left;
                    replaced.right = currNode.right;
                }
                currNode.nodeLock.unlock();
                treeLock.unlock();
            }
        }
        return true;
    }


    private Node replaceNode(Node p) {
        Node currNode;
        Node prevNode = p;

        if (p.left != null) {
            currNode = p.left;
            currNode.nodeLock.lock();

            while (currNode.right != null) {
                if (prevNode != p) {
                    prevNode.nodeLock.unlock();
                }
                prevNode = currNode;
                currNode = currNode.right;
                currNode.nodeLock.lock();
            }

            if (currNode.left != null) {
                currNode.left.nodeLock.lock();
            }
            if (prevNode == p) {
                prevNode.left = currNode.left;
            } else {
                prevNode.right = currNode.left;
                prevNode.nodeLock.unlock();
            }
            if (currNode.left != null) {
                currNode.left.nodeLock.unlock();
            }

            currNode.nodeLock.unlock();
        } else if (p.right != null) {
            currNode = p.right;
            currNode.nodeLock.lock();

            while (currNode.left != null) {
                if (prevNode != p) {
                    prevNode.nodeLock.unlock();
                }
                prevNode = currNode;
                currNode = currNode.left;
                currNode.nodeLock.lock();
            }
            if (currNode.right != null) {
                currNode.right.nodeLock.lock();
            }
            if (prevNode == p) {
                prevNode.right = currNode.right;
            } else {
                prevNode.left = currNode.right;
                prevNode.nodeLock.unlock();
            }
            if (currNode.right != null) {
                currNode.right.nodeLock.unlock();
            }

            currNode.nodeLock.unlock();

        } else {
            return null;
        }
        return currNode;

    }


    private class Node {
        private int data;
        private Node left, right;
        ReentrantLock nodeLock;

        public Node(int data, Node l, Node r) {
            left = l;
            right = r;
            this.data = data;
            nodeLock = new ReentrantLock();

        }


        public Node(int data) {
            this(data, null, null);
        }

        public String toString() {
            return "" + data;
        }
    }
}
