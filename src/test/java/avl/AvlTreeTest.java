package avl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created with IntelliJ IDEA. User: Antonio J. Nebro Date: 08/07/13
 */
class AvlTreeTest {

    private AvlTree<Integer> avlTree;
    private Comparator<?> comparator;

    @BeforeEach
    public void setUp() {
        comparator = Comparator.comparingInt((Integer o) -> o);
        avlTree = new AvlTree<>(comparator);
    }

    @AfterEach
    public void tearDown() {
        avlTree = null;
        comparator = null;
    }

    @Nested
    @DisplayName("When comparing nodes")
    class NodeComparison {
        private AvlNode<Integer> fourNode = getNode(4);
        private AvlNode<Integer> fiveNode = getNode(5);
        private AvlNode<Integer> fiveNodeCopy = getNode(5);

        @Test
        @DisplayName("Returns -1 when comparing a node whose item is lower than the other node item")
        void returnsMinusOneWhenFirstNodeIsLower() {
            assertThat(avlTree.compareNodes(fourNode, fiveNode)).isEqualTo(-1);
        }

        @Test
        @DisplayName("Returns 1 when comparing a node whose item is higher than the other node item")
        void returnsOneWhenFirstNodeIsHigher() {
            assertThat(avlTree.compareNodes(fiveNodeCopy, fourNode)).isEqualTo(1);
        }

        @Test
        @DisplayName("Returns 0 when comparing a node whose item is the same than the other node item")
        void returnsZeroWhenTwoNodesAreTheSame() {
            assertThat(avlTree.compareNodes(fiveNode, fiveNodeCopy)).isZero();
        }
    }

    @Nested
    @DisplayName("Given a newly created AVL Tree")
    class newTree {

        @Test
        @DisplayName("The tree is empty")
        void avlIsEmptyWhenCreated() {
            assertThat(avlTree.avlIsEmpty()).isTrue();
        }

        @Test
        @DisplayName("The tree is not empty when inserting a new node")
        void avlIsNotEmptyWhenAddingANewNode() {
            avlTree.insertAvlNode(getDummyNode());
            assertThat(avlTree.avlIsEmpty()).isFalse();
        }

        @Test
        @DisplayName("Returns null when searching a node")
        void nodeIsNotFoundWhenSearchingItAtAnEmptyTree() {
            AvlNode<Integer> node = avlTree.search(5);
            assertThat(node).isNull();
        }


        @Test
        @DisplayName("When a new node is inserted at the top, then top is equal to the new node")
        void testInsertTop() {
            AvlNode<Integer> node = getNode(4);
            String expectedTree = " | 4";

            avlTree.insertTop(node);

            AvlNode<Integer> obtainedNode = avlTree.getTop();

            assertThat(obtainedNode).isEqualTo(node);
            assertThat(avlTree).hasToString(expectedTree);
        }

        @Test
        @DisplayName("When adding an initial node and then another node with a lower value, then node.getLeft returns the last node")
        void testInsertingLeftElement() {
            AvlNode<Integer> node = new AvlNode<>(6);
            avlTree.insertAvlNode(node);
            AvlNode<Integer> nodeLeft = new AvlNode<>(4);
            avlTree.insertAvlNode(nodeLeft);

            assertThat(node).isEqualTo(nodeLeft.getParent());
            assertThat(nodeLeft).isEqualTo(node.getLeft());

            String tree = " | 6 | 4";
            assertThat(tree).isEqualTo(avlTree.toString());
        }

        @Test
        @DisplayName("When adding an initial node and then another node with a bigger value, then node.getRight returns the last node")
        void testInsertingRightElement() {
            AvlNode<Integer> node = new AvlNode<>(6);
            avlTree.insertAvlNode(node);
            AvlNode<Integer> nodeRight = new AvlNode<>(9);
            avlTree.insertAvlNode(nodeRight);

            assertThat(node).isEqualTo(nodeRight.getParent());
            assertThat(nodeRight).isEqualTo(node.getRight());

            String tree = " | 6 | 9";
            assertThat(tree).isEqualTo(avlTree.toString());
        }
    }

    @Nested
    @DisplayName("Given an AVL Tree with elements")
    class treeWithElements {

        @BeforeEach
        void setUp() {
            comparator = Comparator.comparingInt((Integer o) -> o);
            avlTree = new AvlTree<>(comparator);
        }

        @Test
        @DisplayName("There's a new top node when previous top node is deleted")
        void newTopNodeWhenTopDeleted() {
            insertNodes(avlTree, 20, 8);
            Integer oldItemAtTheTop = avlTree.getTop().getItem();
            avlTree.delete(20);

            AvlNode<Integer> newItemAtTheTop = avlTree.getTop();
            assertThat(newItemAtTheTop.getItem()).isNotEqualTo(oldItemAtTheTop);
        }

        @Test
        @DisplayName("' | 8 | 3 | 2 | 7 | 20 | 9' tree should be empty when deleting every node")
        void delete_WhenDeletingEveryNode_ReturnsEmptyTree() {
            insertNodes(avlTree, 7, 9, 2, 8, 3);

            String tree = " | 7 | 2 | 3 | 9 | 8";
            assertThat(avlTree).hasToString(tree);

            avlTree.delete(3); // right leaf node

            assertThat(avlTree).hasToString(" | 7 | 2 | 9 | 8");

            avlTree.delete(8); // left leaf node

            assertThat(avlTree).hasToString(" | 7 | 2 | 9");

            avlTree.delete(2); // left leaf node

            assertThat(avlTree).hasToString(" | 7 | 9");

            avlTree.delete(9); // right leaf node

            assertThat(avlTree).hasToString(" | 7");

            avlTree.delete(7); // left leaf node

            assertThat(avlTree.getTop()).isNull();
            assertThat(avlTree).hasToString("");
        }

        @DisplayName("The tree (7, 4, 9, 3, 5) is balanced and height is calculated")
        @Test
        void testHeightAndBalanceOfASimpleBalancedTree() {
            AvlNode<Integer> node1, node2, node3, node4, node5;

            node1 = new AvlNode<>(7);
            avlTree.insertAvlNode(node1);
            assertThat(node1.getHeight()).isZero();
            assertThat(avlTree.getBalance(node1)).isZero();

            node2 = new AvlNode<>(4);
            avlTree.insertAvlNode(node2);
            assertThat(node2.getHeight()).isZero();
            assertThat(node1.getHeight()).isEqualTo(1);
            assertThat(avlTree.getBalance(node1)).isEqualTo(-1);
            assertThat(avlTree.getBalance(node2)).isZero();

            node3 = new AvlNode<>(9);
            avlTree.insertAvlNode(node3);
            assertThat(node3.getHeight()).isZero();
            assertThat(node1.getHeight()).isEqualTo(1);
            assertThat(avlTree.getBalance(node1)).isZero();
            assertThat(avlTree.getBalance(node3)).isZero();

            node4 = new AvlNode<>(3);
            avlTree.insertAvlNode(node4);
            assertThat(node4.getHeight()).isZero();
            assertThat(node2.getHeight()).isEqualTo(1);
            assertThat(node1.getHeight()).isEqualTo(2);
            assertThat(avlTree.getBalance(node2)).isEqualTo(-1);
            assertThat(avlTree.getBalance(node1)).isEqualTo(-1);
            assertThat(avlTree.getBalance(node4)).isZero();

            node5 = new AvlNode<>(5);
            avlTree.insertAvlNode(node5);
            assertThat(node5.getHeight()).isZero();
            assertThat(node2.getHeight()).isEqualTo(1);
            assertThat(node1.getHeight()).isEqualTo(2);
            assertThat(avlTree.getBalance(node2)).isZero();
            assertThat(avlTree.getBalance(node1)).isEqualTo(-1);
            assertThat(avlTree.getBalance(node5)).isZero();

            String tree = " | 7 | 4 | 3 | 5 | 9";
            assertThat(avlTree)
                    .hasToString(tree);
        }

        @DisplayName("The tree (7, 4, 3, 2, 1) is balanced and height is calculated")
        @Test
        void tree7_4_3_2_1_isBalanceAndHeightCalculated() {
            AvlNode<Integer> node1, node2, node3, node4, node5;

            node1 = new AvlNode<>(7);
            node2 = new AvlNode<>(4);
            node3 = new AvlNode<>(3);
            node4 = new AvlNode<>(2);
            node5 = new AvlNode<>(1);

            avlTree.insertAvlNode(node1);
            avlTree.insertAvlNode(node2);
            avlTree.insertAvlNode(node3);
            avlTree.insertAvlNode(node4);
            avlTree.insertAvlNode(node5);

            assertThat(node2).isEqualTo(avlTree.getTop());
            assertThat(node4).isEqualTo(node2.getLeft());
            assertThat(node1).isEqualTo(node2.getRight());
            assertThat(node5).isEqualTo(node4.getLeft());
            assertThat(node3).isEqualTo(node4.getRight());
            assertThat(node1.getHeight()).isZero();
            assertThat(node2.getHeight()).isEqualTo(2);
            assertThat(node4.getHeight()).isEqualTo(1);

            String tree = " | 4 | 2 | 1 | 3 | 7";
            assertThat(tree).isEqualTo(avlTree.toString());
        }

        @Test
        @DisplayName("The tree (7, 8, 9, 10, 11) is balanced and height is calculated")
        void tree7_8_9_10_11_isBalanceAndHeightCalculated() {
            AvlNode<Integer> node1, node2, node3, node4, node5;

            node1 = new AvlNode<>(7);
            node2 = new AvlNode<>(8);
            node3 = new AvlNode<>(9);
            node4 = new AvlNode<>(10);
            node5 = new AvlNode<>(11);

            avlTree.insertAvlNode(node1);
            avlTree.insertAvlNode(node2);
            avlTree.insertAvlNode(node3);
            avlTree.insertAvlNode(node4);
            avlTree.insertAvlNode(node5);

            assertEquals(node2, avlTree.getTop());
            assertEquals(node4, node2.getRight());
            assertEquals(node1, node2.getLeft());
            assertEquals(node5, node4.getRight());
            assertEquals(node3, node4.getLeft());
            assertEquals(2, avlTree.getTop().getHeight());
            assertEquals(1, node4.getHeight());
            assertEquals(0, node1.getHeight());

            String tree = " | 8 | 7 | 10 | 9 | 11";
            assertEquals(tree, avlTree.toString());
        }

        @Test
        @DisplayName("The tree (7, 10, 14), two double right nodes is rebalanced")
        void testInsertingRightRightNodeAndRebalance() {
            AvlNode<Integer> node1, node2, node3;

            node1 = new AvlNode<>(7);
            avlTree.insertAvlNode(node1);

            node2 = new AvlNode<>(10);
            avlTree.insertAvlNode(node2);

            node3 = new AvlNode<>(14);
            avlTree.insertAvlNode(node3);

            assertThat(node2).isEqualTo(avlTree.getTop());
            assertThat(node1).isEqualTo(node2.getLeft());
            assertThat(node3).isEqualTo(node2.getRight());

            assertThat(avlTree.getTop().getHeight()).isEqualTo(1);
            assertThat(avlTree.getTop().getLeft().getHeight()).isZero();
            assertThat(avlTree.getTop().getRight().getHeight()).isZero();

            assertThat(-1).isEqualTo(avlTree.height(node1.getLeft()));
            assertThat(-1).isEqualTo(avlTree.height(node1.getRight()));
            assertThat(-1).isEqualTo(avlTree.height(node3.getLeft()));
            assertThat(-1).isEqualTo(avlTree.height(node3.getRight()));

            String tree = " | 10 | 7 | 14";
            assertThat(avlTree).hasToString(tree);
        }

        @Test
        @DisplayName("The tree (7, 2, 3), two double right nodes, is rebalanced")
        void testInsertingLeftRightNodeAndRebalance() {
            AvlNode<Integer> node1, node2, node3;

            node1 = new AvlNode<>(7);
            avlTree.insertAvlNode(node1);

            node2 = new AvlNode<>(2);
            avlTree.insertAvlNode(node2);

            node3 = new AvlNode<>(3);
            avlTree.insertAvlNode(node3);

            assertEquals(node3, avlTree.getTop());
            assertEquals(node2, node3.getLeft());
            assertEquals(node1, node3.getRight());

            assertEquals(1,
                    avlTree.getTop().getHeight());
            assertEquals(0,
                    avlTree.getTop().getLeft().getHeight());
            assertEquals(0,
                    avlTree.getTop().getRight().getHeight());

            assertEquals(-1, avlTree.height(node2.getLeft()));
            assertEquals(-1, avlTree.height(node2.getRight()));
            assertEquals(-1, avlTree.height(node1.getLeft()));
            assertEquals(-1, avlTree.height(node1.getRight()));

            String tree = " | 3 | 2 | 7";
            assertEquals(tree, avlTree.toString());
        }

        @Test
        @DisplayName("The tree (7, 4, 3), two double left nodes, then the tree is rebalanced")
        void testInsertingLeftLeftNodeAndRebalance() {
            AvlNode<Integer> node1, node2, node3;

            node1 = new AvlNode<>(7);
            avlTree.insertAvlNode(node1);

            node2 = new AvlNode<>(4);
            avlTree.insertAvlNode(node2);

            node3 = new AvlNode<>(3);
            avlTree.insertAvlNode(node3);

            assertThat(avlTree.getTop()).isEqualTo(node2);
            assertThat(node2.getLeft()).isEqualTo(node3);
            assertThat(node2.getRight()).isEqualTo(node1);

            assertThat(avlTree.getTop().getHeight()).isEqualTo(1);
            assertThat(avlTree.getTop().getLeft().getHeight()).isZero();
            assertThat(avlTree.getTop().getRight().getHeight()).isZero();
            assertThat(avlTree.height(node1.getLeft())).isEqualTo(-1);
            assertThat(avlTree.height(node1.getRight())).isEqualTo(-1);
            assertThat(avlTree.height(node3.getLeft())).isEqualTo(-1);
            assertThat(avlTree.height(node3.getRight())).isEqualTo(-1);

            String tree = " | 4 | 3 | 7";
            assertThat(tree).isEqualTo(avlTree.toString());
        }

        @Test
        @DisplayName("Return valid successors")
        void testFindSuccessor() {
            insertNodes(avlTree, 20, 8, 22, 4, 12, 24, 10, 14);
            AvlNode<Integer> node;

            node = avlTree.search(8);
            assertEquals(avlTree.search(10), avlTree.findSuccessor(node));
            node = avlTree.search(10);
            assertEquals(avlTree.search(12), avlTree.findSuccessor(node));
            node = avlTree.search(14);
            assertEquals(avlTree.search(20), avlTree.findSuccessor(node));

            String tree = " | 20 | 8 | 4 | 12 | 10 | 14 | 22 | 24";
            assertThat(avlTree).hasToString(tree);
        }

        @ParameterizedTest(name = "With node {0}")
        @DisplayName("When nodes exists, then return valid node")
        @ValueSource(ints = {7, 9, 8, 2, 3, 6, 4, 10})
        void search_WhenNodeWithItemExist_ReturnValidNode(int toSearch) {
            insertNodes(avlTree, 7, 9, 6, 4, 10, 8, 2, 3);

            assertThat(avlTree.search(toSearch).getItem())
                    .isEqualTo(toSearch);

            String tree = " | 7 | 4 | 2 | 3 | 6 | 9 | 8 | 10";
            assertThat(avlTree)
                    .hasToString(tree);
        }

    }

    @Nested
    @DisplayName("The tree is rebalanced")
    class treeIsRebalanced {

        @Test
        @DisplayName("When deleting a node")
        void treeIsRebalancedWhenDeletingANode() {
            insertNodes(avlTree, 22, 4, 12, 24, 10, 14);

            avlTree.delete(22);
            AvlNode<Integer> top = avlTree.getTop();

            assertThat(top.getItem()).isEqualTo(12);
            assertThat(top.getLeft()).isEqualTo(avlTree.search(4));
            assertThat(top.getRight()).isEqualTo(avlTree.search(24));
        }

        @Test
        @DisplayName("When deleting a node with two leaves")
        void treeIsRebalancedWhenDeletingANodeWithTwoLeaves() {
            insertNodes(avlTree, 22, 4, 12, 24, 10, 14);

            avlTree.delete(12);
            avlTree.delete(8);

            assertThat(avlTree.getTop().getLeft().getItem()).isEqualTo(4);
            assertThat(avlTree).hasToString(" | 14 | 4 | 10 | 22 | 24");
        }

        @Test
        @DisplayName("When deleting a node with one right leaf")
        void treeIsRebalancedWhenDeletingANodeWithOneRightLeaf() {
            insertNodes(avlTree, 20, 8, 25, 28, 12, 10, 14);
            avlTree.delete(20);

            assertThat(avlTree.getTop().getItem()).isEqualTo(12);
        }

        @Test
        @DisplayName("When inserting both left and right node")
        void testInsertingRightLeftNodeAndRebalanced() {
            AvlNode<Integer> node1, node2, node3;
            node1 = new AvlNode<>(7);
            node2 = new AvlNode<>(9);
            node3 = new AvlNode<>(8);

            avlTree.insertAvlNode(node1);
            avlTree.insertAvlNode(node2);
            avlTree.insertAvlNode(node3);

            assertThat(avlTree.getTop()).isEqualTo(node3);
            assertThat(node3.getLeft()).isEqualTo(node1);
            assertThat(node3.getRight()).isEqualTo(node2);

            assertThat(avlTree.getTop().getHeight()).isEqualTo(1);
            assertThat(avlTree.getTop().getLeft().getHeight()).isZero();
            assertThat(avlTree.getTop().getRight().getHeight()).isZero();

            assertThat(avlTree.height(node2.getLeft())).isEqualTo(-1);
            assertThat(avlTree.height(node2.getRight())).isEqualTo(-1);
            assertThat(avlTree.height(node1.getLeft())).isEqualTo(-1);
            assertThat(avlTree.height(node1.getRight())).isEqualTo(-1);

            String tree = " | 8 | 7 | 9";
            assertThat(avlTree).hasToString(tree);
        }
    }

    @Nested
    @DisplayName("When a node is inserted")
    class insertedNode {

        @Test
        @DisplayName("and two nodes are inserted in right and left respectively, then searchClosestNode returns their respective value for each one")
        void testInsertingRightAndLeftElementsJustAfterTop() {
            AvlNode<Integer> node = new AvlNode<>(6);
            avlTree.insertAvlNode(node);

            AvlNode<Integer> nodeLeft = new AvlNode<>(4);
            AvlNode<Integer> nodeRight = new AvlNode<>(9);

            assertThat(avlTree.searchClosestNode(nodeLeft))
                    .isEqualTo(-1);
            assertThat(avlTree.searchClosestNode(nodeRight))
                    .isEqualTo(1);
            assertThat(avlTree.searchClosestNode(node))
                    .isZero();

            assertThat(nodeLeft.getClosestNode())
                    .isEqualTo(node);
            assertThat(nodeRight.getClosestNode())
                    .isEqualTo(node);

            node.setLeft(nodeLeft);
            node.setRight(nodeRight);

            AvlNode<Integer> nodeRightLeft = new AvlNode<>(7);

            assertThat(avlTree.searchClosestNode(nodeRightLeft))
                    .isEqualTo(-1);
            assertThat(nodeRightLeft.getClosestNode())
                    .isEqualTo(nodeRight);

            AvlNode<Integer> nodeLeftRight = new AvlNode<>(5);
            assertThat(avlTree.searchClosestNode(nodeLeftRight))
                    .isEqualTo(1);
            assertThat(nodeLeft).isEqualTo(nodeLeftRight.getClosestNode());

            String tree = " | 6 | 4 | 9";
            assertThat(tree).isEqualTo(avlTree.toString());
        }
    }

    @DisplayName("When some nodes are inserted")
    @Nested
    class someNodesInserted {

        @DisplayName("(7, 4, 9, 6, 8), then searchClosest returns 1, -1 or 0 depending on their value")
        @Test
        void testSearchClosestNode() {
            int result;
            AvlNode<Integer> node = new AvlNode<>(7);
            result = avlTree.searchClosestNode(node);
            assertThat(0).isEqualTo(result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(4);
            result = avlTree.searchClosestNode(node);
            assertThat(-1).isEqualTo(result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(9);
            result = avlTree.searchClosestNode(node);
            assertThat(1).isEqualTo(result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(6);
            result = avlTree.searchClosestNode(node);
            assertThat(1).isEqualTo(result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(8);
            result = avlTree.searchClosestNode(node);
            assertThat(-1).isEqualTo(result);
            avlTree.insertAvlNode(node);

            String tree = " | 7 | 4 | 6 | 9 | 8";
            assertThat(tree).isEqualTo(avlTree.toString());
        }

    }


    private <T> void insertNodes(AvlTree<T> avlTree, T... objects) {
        for (T object : objects) {
            avlTree.insert(object);
        }
    }

    private <T> AvlNode<T> getDummyNode() {
        return Mockito.mock(AvlNode.class);
    }

    private <T> AvlNode<T> getNode(T item) {
        return new AvlNode<>(item);
    }
}
