package ParBST;

 /***************************************************************************
 * The sequential Binary Search Tree (for storing int values)
 ****************************************************************************/

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWBST {

    private Node root;
    private ReentrantLock treeLock;

    public RWBST() {
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
            currNode.nodeLock.writeLock().lock();
            treeLock.unlock();

            while (true) {
                if (data == currNode.data) {
                    currNode.nodeLock.writeLock().unlock();
                    return false;
                }

                if (data < currNode.data) {
                    if (currNode.left == null) {
                        currNode.left = new Node(data);
                        currNode.nodeLock.writeLock().unlock();
                        return true;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.left;
                        currNode.nodeLock.writeLock().lock();
                        prevNode.nodeLock.writeLock().unlock();

                    }
                } else {
                    if (currNode.right == null) {
                        currNode.right = new Node(data);
                        currNode.nodeLock.writeLock().unlock();
                        return true;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.right;
                        currNode.nodeLock.writeLock().lock();
                        prevNode.nodeLock.writeLock().unlock();
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
            currNode.nodeLock.readLock().lock();
            treeLock.unlock();

            while (true) {
                if (toSearch == currNode.data) {
                    currNode.nodeLock.readLock().unlock();
                    return true;
                }

                if (toSearch < currNode.data) {
                    if (currNode.left == null) {
                        currNode.nodeLock.readLock().unlock();
                        return false;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.left;
                        currNode.nodeLock.readLock().lock();
                        prevNode.nodeLock.readLock().unlock();

                    }
                } else {
                    if (currNode.right == null) {
                        currNode.nodeLock.readLock().unlock();
                        return false;
                    } else {
                        prevNode = currNode;
                        currNode = currNode.right;
                        currNode.nodeLock.readLock().lock();
                        prevNode.nodeLock.readLock().unlock();
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
            currNode.nodeLock.writeLock().lock();

            if (currNode.data != toDelete) {
                prevNode = currNode;
                if (currNode.data > toDelete) {
                    currNode = currNode.left;
                } else {
                    currNode = currNode.right;
                }
                currNode.nodeLock.writeLock().lock();
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

                        currNode.nodeLock.writeLock().unlock();
                        prevNode.nodeLock.writeLock().unlock();

                        break;

                    } else {
                        prevNode.nodeLock.writeLock().unlock();
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
                        currNode.nodeLock.writeLock().lock();
                    }
                }
            } else {
                Node replaced = replaceNode(currNode);
                root = replaced;

                if (replaced != null) {
                    replaced.left = currNode.left;
                    replaced.right = currNode.right;
                }
                currNode.nodeLock.writeLock().unlock();
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
            currNode.nodeLock.writeLock().lock();

            while (currNode.right != null) {
                if (prevNode != p) {
                    prevNode.nodeLock.writeLock().unlock();
                }
                prevNode = currNode;
                currNode = currNode.right;
                currNode.nodeLock.writeLock().lock();
            }

            if (currNode.left != null) {
                currNode.left.nodeLock.writeLock().lock();
            }
            if (prevNode == p) {
                prevNode.left = currNode.left;
            } else {
                prevNode.right = currNode.left;
                prevNode.nodeLock.writeLock().unlock();
            }
            if (currNode.left != null) {
                currNode.left.nodeLock.writeLock().unlock();
            }

            currNode.nodeLock.writeLock().unlock();
        } else if (p.right != null) {
            currNode = p.right;
            currNode.nodeLock.writeLock().lock();

            while (currNode.left != null) {
                if (prevNode != p) {
                    prevNode.nodeLock.writeLock().unlock();
                }
                prevNode = currNode;
                currNode = currNode.left;
                currNode.nodeLock.writeLock().lock();
            }
            if (currNode.right != null) {
                currNode.right.nodeLock.writeLock().lock();
            }
            if (prevNode == p) {
                prevNode.right = currNode.right;
            } else {
                prevNode.left = currNode.right;
                prevNode.nodeLock.writeLock().unlock();
            }
            if (currNode.right != null) {
                currNode.right.nodeLock.writeLock().unlock();
            }

            currNode.nodeLock.writeLock().unlock();

        } else {
            return null;
        }
        return currNode;

    }


    private class Node {
        private int data;
        private Node left, right;
        ReentrantReadWriteLock nodeLock;

        public Node(int data, Node l, Node r) {
            left = l;
            right = r;
            this.data = data;
            nodeLock = new ReentrantReadWriteLock();

        }


        public Node(int data) {
            this(data, null, null);
        }

        public String toString() {
            return "" + data;
        }
    }
}
