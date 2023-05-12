package avl;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA. User: Antonio J. Nebro Date: 08/07/13
 */
@DisplayName("Given a new AvlTree")
@Nested
public class AvlTreeTest {

    AvlTree<Integer> avlTree;
    Comparator<?> comparator;

    @BeforeEach
    public void setUp() {
        comparator = Comparator.comparingInt((Integer o) -> o);
        avlTree = new AvlTree(comparator);
    }

    @AfterEach
    public void tearDown() {
        avlTree = null;
        comparator = null;
    }

    @Test
    @DisplayName("When a new node is inserted, then the tree is not empty")
    public void avlIsEmpty_WhenNodeIsInserted_ReturnFalse() {
        assertThat(avlTree.avlIsEmpty()).isTrue();

        avlTree.insertAvlNode(getDummyNode());

        assertThat(avlTree.avlIsEmpty()).isFalse();
    }


    @Test
    @DisplayName("When a new node is inserted at the top, then top is equal to the new node")
    public void testInsertTop() {
        AvlNode<Integer> node = getNode(4);
        String expectedTree = " | 4";

        avlTree.insertTop(node);

        AvlNode<Integer> topNodeAfterInserting = avlTree.getTop();
        String obtainedTree = avlTree.toString();
        
        assertEquals(node, topNodeAfterInserting);
        assertEquals(expectedTree, obtainedTree);
    }

    @Test
    @DisplayName("When nodes are compared, then they are compared by the value")
    public void testCompareNodes() {
        AvlNode<Integer> node1 = getNode(4);
        AvlNode<Integer> node2 = getNode(5);
        AvlNode<Integer> node3 = getNode(5);

        assertAll(
                () -> {
                    assertThat(avlTree.compareNodes(node1, node2)).isEqualTo(-1);
                },
                () -> {
                    assertThat(avlTree.compareNodes(node3, node1)).isEqualTo(1);
                },
                () -> {
                    assertThat(avlTree.compareNodes(node2, node3)).isEqualTo(0);
                }
        );
    }

    @Nested
    @DisplayName("When a node is inserted")
    class insertedNode {

        @Test
        @DisplayName("and two nodes are inserted in right and left respectively, then searchClosestNode returns their respective value for each one")
        public void testInsertingRightAndLeftElementsJustAfterTop() {
            AvlNode<Integer> node = new AvlNode<>(6);
            avlTree.insertAvlNode(node);
            AvlNode<Integer> nodeLeft = new AvlNode<>(4);
            AvlNode<Integer> nodeRight = new AvlNode<>(9);

            assertThat(avlTree.searchClosestNode(nodeLeft))
                    .isEqualTo(-1);
            assertThat(avlTree.searchClosestNode(nodeRight))
                    .isEqualTo(1);
            assertThat(avlTree.searchClosestNode(node))
                    .isEqualTo(0);

            assertThat(nodeLeft.getClosestNode())
                    .isEqualTo(node);
            assertThat(nodeRight.getClosestNode())
                    .isEqualTo(node);

            node.setLeft(nodeLeft);
            node.setRight(nodeRight);
            AvlNode<Integer> nodeRightLeft = new AvlNode<>(7);
            avlTree.searchClosestNode(nodeRightLeft);

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

        @Test
        @DisplayName("and a node with a lower value, then node.getLeft returns the node")
        public void testInsertingLeftElement() {
            AvlNode<Integer> node = new AvlNode<>(6);
            avlTree.insertAvlNode(node);
            AvlNode<Integer> nodeLeft = new AvlNode<>(4);
            avlTree.insertAvlNode(nodeLeft);

            assertThat(node).isEqualTo(nodeLeft.getParent());
            assertThat(nodeLeft).isEqualTo(node.getLeft());

            String tree = " | 6 | 4";
            assertThat(tree).isEqualTo(avlTree.toString());
        }

        @DisplayName("and a node with a bigger value is inserted, then node.getRight returns the node")
        @Test
        public void testInsertingRightElement() {
            AvlNode<Integer> node = new AvlNode<>(6);
            avlTree.insertAvlNode(node);
            AvlNode<Integer> nodeRight = new AvlNode<>(9);
            avlTree.insertAvlNode(nodeRight);

            assertThat(node).isEqualTo(nodeRight.getParent());
            assertEquals(nodeRight, node.getRight());

            String tree = " | 6 | 9";
            assertEquals(tree, avlTree.toString());
        }
    }

    @DisplayName("When some nodes are inserted")
    @Nested
    class someNodesInserted {

        @DisplayName("(7, 4, 9, 6, 8), then searchClosest returns 1, -1 or 0 depending on their value")
        @Test
        public void testSearchClosestNode() {
            int result;
            AvlNode<Integer> node = new AvlNode<>(7);
            result = avlTree.searchClosestNode(node);
            assertEquals(0, result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(4);
            result = avlTree.searchClosestNode(node);
            assertEquals(-1, result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(9);
            result = avlTree.searchClosestNode(node);
            assertEquals(1, result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(6);
            result = avlTree.searchClosestNode(node);
            assertEquals(1, result);
            avlTree.insertAvlNode(node);

            node = new AvlNode<>(8);
            result = avlTree.searchClosestNode(node);
            assertEquals(-1, result);
            avlTree.insertAvlNode(node);

            String tree = " | 7 | 4 | 6 | 9 | 8";
            assertEquals(tree, avlTree.toString());
        }




        @DisplayName("(7, 4, 9, 3, 5), then the tree is balanced and height is calculated")
        @Test
        public void testHeightAndBalanceOfASimpleBalancedTree() {
            AvlNode<Integer> node1, node2, node3, node4, node5;

            node1 = new AvlNode<>(7);
            avlTree.insertAvlNode(node1);
            assertEquals(0, node1.getHeight());
            assertEquals(0, avlTree.getBalance(node1));

            node2 = new AvlNode<>(4);
            avlTree.insertAvlNode(node2);
            assertEquals(0, node2.getHeight());
            assertEquals(1, node1.getHeight());
            assertEquals(-1, avlTree.getBalance(node1));
            assertEquals(0, avlTree.getBalance(node2));

            node3 = new AvlNode<>(9);
            avlTree.insertAvlNode(node3);
            assertEquals(0, node3.getHeight());
            assertEquals(1, node1.getHeight());
            assertEquals(0, avlTree.getBalance(node1));
            assertEquals(0, avlTree.getBalance(node3));

            node4 = new AvlNode<>(3);
            avlTree.insertAvlNode(node4);
            assertEquals(0, node4.getHeight());
            assertEquals(1, node2.getHeight());
            assertEquals(2, node1.getHeight());
            assertEquals(-1, avlTree.getBalance(node2));
            assertEquals(-1, avlTree.getBalance(node1));
            assertEquals(0, avlTree.getBalance(node4));

            node5 = new AvlNode<>(5);
            avlTree.insertAvlNode(node5);
            assertEquals(0, node5.getHeight());
            assertEquals(1, node2.getHeight());
            assertEquals(2, node1.getHeight());
            assertEquals(0, avlTree.getBalance(node2));
            assertEquals(-1, avlTree.getBalance(node1));
            assertEquals(0, avlTree.getBalance(node5));

            String tree = " | 7 | 4 | 3 | 5 | 9";
            assertEquals(tree, avlTree.toString());
        }

        /**
         * Testing adding 7 - 4 - 3
         */
        @Test
        public void testInsertingLeftLeftNodeAndRebalance() {
            AvlNode<Integer> node1, node2, node3, node4, node5;

            node1 = new AvlNode<>(7);
            avlTree.insertAvlNode(node1);
            assertEquals(0, node1.getHeight());
            assertEquals(0, avlTree.getBalance(node1));

            node2 = new AvlNode<>(4);
            avlTree.insertAvlNode(node2);
            assertEquals(0, node2.getHeight());
            assertEquals(1, node1.getHeight());
            assertEquals(-1, avlTree.getBalance(node1));
            assertEquals(0, avlTree.getBalance(node2));

            node3 = new AvlNode<>(3);
            avlTree.insertAvlNode(node3);
            assertEquals(node2, avlTree.getTop());
            assertEquals(node3, node2.getLeft());
            assertEquals(node1, node2.getRight());

            assertEquals(1, avlTree.getTop().getHeight());
            assertEquals(0,
                    avlTree.getTop().getLeft().getHeight());
            assertEquals(0,
                    avlTree.getTop().getRight().getHeight());
            assertEquals(-1, avlTree.height(node1.getLeft()));
            assertEquals(-1, avlTree.height(node1.getRight()));
            assertEquals(-1, avlTree.height(node3.getLeft()));
            assertEquals(-1, avlTree.height(node3.getRight()));

            String tree = " | 4 | 3 | 7";
            assertEquals(tree, avlTree.toString());
        }
    }


    /**
     * Testing adding 7 - 10 - 14
     */
    @Test
    public void testInsertingRightRightNodeAndRebalance() {
        AvlNode<Integer> node1, node2, node3, node4, node5;

        node1 = new AvlNode<>(7);
        avlTree.insertAvlNode(node1);
        assertEquals(0, node1.getHeight());
        assertEquals(0, avlTree.getBalance(node1));

        node2 = new AvlNode<>(10);
        avlTree.insertAvlNode(node2);
        assertEquals(0, node2.getHeight());
        assertEquals(1, node1.getHeight());
        assertEquals(1, avlTree.getBalance(node1));
        assertEquals(0, avlTree.getBalance(node2));

        node3 = new AvlNode<>(14);
        avlTree.insertAvlNode(node3);
        assertEquals(node2, avlTree.getTop());
        assertEquals(node1, node2.getLeft());
        assertEquals(node3, node2.getRight());

        assertEquals(1, avlTree.getTop().getHeight());
        assertEquals(0,
                avlTree.getTop().getLeft().getHeight());
        assertEquals(0,
                avlTree.getTop().getRight().getHeight());
        assertEquals(-1, avlTree.height(node1.getLeft()));
        assertEquals(-1, avlTree.height(node1.getRight()));
        assertEquals(-1, avlTree.height(node3.getLeft()));
        assertEquals(-1, avlTree.height(node3.getRight()));

        String tree = " | 10 | 7 | 14";
        assertEquals(tree, avlTree.toString());
    }

    /**
     * Testing adding 7 - 4 - 3 - 2 - 1
     */
    @Test
    public void testInserting7_4_3_2_1() {
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

        assertEquals(node2, avlTree.getTop());
        assertEquals(node4, node2.getLeft());
        assertEquals(node1, node2.getRight());
        assertEquals(node5, node4.getLeft());
        assertEquals(node3, node4.getRight());
        assertEquals(0, node1.getHeight());
        assertEquals(2, node2.getHeight());
        assertEquals(1, node4.getHeight());

        String tree = " | 4 | 2 | 1 | 3 | 7";
        assertEquals(tree, avlTree.toString());
    }

    /**
     * Testing adding 7 - 4 - 3 - 2 - 1
     */
    @Test
    public void testInserting7_8_9_10_11() {
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

    /**
     * Testing adding 7 - 2 - 3
     */
    @Test
    public void testInsertingLeftRightNodeAndRebalance() {
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

        assertEquals(1, avlTree.getTop().getHeight());
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

    /**
     * Testing adding 7 - 9 - 8
     */
    @Test
    public void testInsertingRightLeftNodeAndRebalance() {
        AvlNode<Integer> node1, node2, node3;

        node1 = new AvlNode<>(7);
        avlTree.insertAvlNode(node1);

        node2 = new AvlNode<>(9);
        avlTree.insertAvlNode(node2);

        node3 = new AvlNode<>(8);
        avlTree.insertAvlNode(node3);

        assertEquals(node3, avlTree.getTop());
        assertEquals(node1, node3.getLeft());
        assertEquals(node2, node3.getRight());

        assertEquals(1, avlTree.getTop().getHeight());
        assertEquals(0,
                avlTree.getTop().getLeft().getHeight());
        assertEquals(0,
                avlTree.getTop().getRight().getHeight());
        assertEquals(-1, avlTree.height(node2.getLeft()));
        assertEquals(-1, avlTree.height(node2.getRight()));
        assertEquals(-1, avlTree.height(node1.getLeft()));
        assertEquals(-1, avlTree.height(node1.getRight()));

        String tree = " | 8 | 7 | 9";
        assertEquals(tree, avlTree.toString());
    }

    @Test
    public void testSearchNode() {
        AvlNode<Integer> node1, node2, node3, node4, node5;

        node1 = new AvlNode<>(7);
        avlTree.insertAvlNode(node1);

        node2 = new AvlNode<>(9);
        avlTree.insertAvlNode(node2);

        node3 = new AvlNode<>(8);
        avlTree.insertAvlNode(node3);

        node4 = new AvlNode<>(2);
        avlTree.insertAvlNode(node4);

        node5 = new AvlNode<>(3);
        avlTree.insertAvlNode(node5);

        assertEquals(node1, avlTree.search(7));
        assertEquals(node2, avlTree.search(9));
        assertEquals(node3, avlTree.search(8));
        assertEquals((Integer) 2,
                avlTree.searchNode(new AvlNode<>(2)).getItem());
        assertEquals(node4, avlTree.search(2));
        assertEquals(node5, avlTree.search(3));
        assertNull(avlTree.search(14));
        assertNull(avlTree.search(0));

        String tree = " | 8 | 3 | 2 | 7 | 9";
        assertEquals(tree, avlTree.toString());
    }

    @Test
    public void testFindSuccessor() {
        AvlNode<Integer> node;

        node = new AvlNode<>(20);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(8);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(22);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(4);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(12);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(24);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(10);
        avlTree.insertAvlNode(node);

        node = new AvlNode<>(14);
        avlTree.insertAvlNode(node);

        node = avlTree.search(8);
        assertEquals(avlTree.search(10), avlTree.findSuccessor(node));
        node = avlTree.search(10);
        assertEquals(avlTree.search(12), avlTree.findSuccessor(node));
        node = avlTree.search(14);
        assertEquals(avlTree.search(20), avlTree.findSuccessor(node));

        String tree = " | 20 | 8 | 4 | 12 | 10 | 14 | 22 | 24";
        assertEquals(tree, avlTree.toString());
    }

    @Test
    public void testDeletingLeafNodes() {
        insertNodes(avlTree,7,9,2,8,3);
        AvlNode<Integer> node1, node2, node3, node4, node5;

        node1 = new AvlNode<>(7);
        avlTree.insertAvlNode(node1);

        node2 = new AvlNode<>(9);
        avlTree.insertAvlNode(node2);

        node3 = new AvlNode<>(2);
        avlTree.insertAvlNode(node3);

        node4 = new AvlNode<>(8);
        avlTree.insertAvlNode(node4);

        node5 = new AvlNode<>(3);
        avlTree.insertAvlNode(node5);

        String tree = " | 7 | 2 | 3 | 9 | 8";
        assertEquals(tree, avlTree.toString());

        avlTree.delete(3); // right leaf node

        assertThat(avlTree.search(2).getRight())
                .isNull();
        assertThat(avlTree.search(2).getHeight())
                .isEqualTo(0);
        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(2);
        assertThat(avlTree)
                .hasToString(" | 7 | 2 | 9 | 8");

        avlTree.delete(8); // left leaf node

        assertThat(avlTree.search(9).getLeft())
                .isNull();
        assertThat(avlTree.search(9).getHeight())
                .isEqualTo(0);
        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(1);
        assertThat(avlTree)
                .hasToString(" | 7 | 2 | 9");

        avlTree.delete(2); // left leaf node

        assertThat(avlTree.search(7).getLeft())
                .isNull();
        assertThat(avlTree.search(7).getHeight())
                .isEqualTo(1);
        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(1);


        assertNull(node1.getLeft());
        assertEquals(1, node1.getHeight());
        assertEquals(" | 7 | 9", avlTree.toString());

        avlTree.delete(9); // right leaf node
        assertNull(node1.getRight());
        assertEquals(0, node1.getHeight());
        assertEquals(" | 7", avlTree.toString());

        avlTree.delete(7); // left leaf node
        assertNull(avlTree.getTop());
        assertEquals("", avlTree.toString());
    }

    @Test
    @DisplayName("When deleting nodes with one leaf, the tree is balanced")
    public void delete_WhenDeletingNodesWithOneLeaf_ThenTreeIsRebalanced() {
        insertNodes(avlTree, 7, 9, 2, 8, 3);

        String tree = " | 7 | 2 | 3 | 9 | 8";
        assertThat(avlTree)
                .hasToString(tree);

        avlTree.delete(2);

        assertThat(avlTree.search(7).getLeft().getItem())
                .isEqualTo(3);
        assertThat(avlTree.search(2).getRight())
                .isNull();
        assertThat(avlTree.search(2).getHeight())
                .isEqualTo(0);
        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(2);
        assertThat(avlTree)
                .hasToString(" | 7 | 3 | 9 | 8");

        avlTree.delete(9);

        assertThat(avlTree.search(7).getRight().getItem())
                .isEqualTo(9);
        assertThat(avlTree.search(9).getLeft())
                .isNull();
        assertThat(avlTree.search(9).getHeight())
                .isEqualTo(0);
        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(1);
        assertThat(avlTree)
                .hasToString(" | 7 | 3 | 8");

    }

    @Test
    @DisplayName("When deleting nodes with two leaves, the tree is balanced")
    public void delete_WhenDeletingNodesWithTwoLeaves_ThenTreeIsRebalanced() {
        AvlNode<Integer> node;

        insertNodes(avlTree, 20, 8, 22, 4, 12, 24, 10, 14);

        String expected = " | 20 | 8 | 4 | 12 | 10 | 14 | 22 | 24";
        assertThat(avlTree)
                .hasToString(expected);

        avlTree.delete(12);
        node = avlTree.search(8);

        assertThat(node.getRight().getItem())
                .isEqualTo(14);
        assertThat(avlTree)
                .hasToString(" | 20 | 8 | 4 | 14 | 10 | 22 | 24");

        avlTree.delete(8);

        assertThat(avlTree.getTop().getLeft().getItem())
                .isEqualTo(10);
        assertThat(avlTree)
                .hasToString(" | 20 | 10 | 4 | 14 | 22 | 24");
    }

    @Test
    @DisplayName("When deleting a node, the tree is rebalanced")
    public void delete_WhenNodeExists_ThenTreeIsRebalanced() {
        insertNodes(avlTree, 20, 8, 22, 4, 12, 24, 10, 14);

        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(3);

        avlTree.delete(22);

        assertThat(avlTree.getTop().getItem())
                .isEqualTo(12);
        assertThat(avlTree.getTop().getLeft())
                .isEqualTo(avlTree.search(8));
        assertThat(avlTree.getTop().getRight())
                .isEqualTo(avlTree.search(20));
    }

    @Test
    @DisplayName("When top node is deleted, there is a new top node")
    public void delete_WhenIsTopNode_ThenTopNodeChanges() {
        String expectedTree = " | 12 | 8 | 4 | 10 | 22 | 14 | 24";

        insertNodes(avlTree, 20, 8, 22, 4, 12, 24, 10, 14);

        assertThat(avlTree.getTop().getHeight())
                .isEqualTo(3);

        avlTree.delete(20);

        assertThat(avlTree)
                .hasToString(expectedTree);
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
