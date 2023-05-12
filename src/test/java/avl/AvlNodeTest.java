package avl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created with IntelliJ IDEA. User: Antonio J. Nebro Date: 09/07/13 Time: 15:29
 */
@DisplayName("Given an AVL node")
class AvlNodeTest {

    private AvlNode<Integer> node;

    @BeforeEach
    public void setUp() {
        node = new AvlNode<>(5);
    }

    @AfterEach
    public void tearDown() {
        node = null;
    }


    @Test
    @DisplayName("When left node is set, node has left")
    void hasLeft_WhenSetLeftNode_ReturnsTrue() {
        AvlNode<Integer> dummyNode = getDummyNode();
        node.setLeft(dummyNode);
        node.updateHeight();

        assertThat(node.hasLeft())
                .isTrue();
        assertThat(node.hasOnlyALeftChild())
                .isTrue();
        assertThat(node.getHeight())
                .isEqualTo(1);
        assertThat(node.getLeft())
                .isEqualTo(dummyNode);
    }

    @Test
    @DisplayName("When right node is set, node has right")
    void hasRight_WhenSetRightNode_ReturnsTrue() {
        AvlNode<Integer> dummyNode = getDummyNode();
        node.setRight(dummyNode);
        node.updateHeight();

        assertThat(node.hasRight())
                .isTrue();
        assertThat(node.hasOnlyARightChild())
                .isTrue();
        assertThat(node.getHeight())
                .isEqualTo(1);
        assertThat(node.getRight())
                .isEqualTo(dummyNode);
    }


    @Test
    @DisplayName("When right and left node is set, height is the highest plus one")
    void getHeight_WhenSetRightAndLeftNode_ReturnsHighestPlusOne() {
        AvlNode<Integer> rightNode = getDummyNode();
        AvlNode<Integer> leftNode = getDummyNode();
        ReflectionTestUtils.setField(rightNode, "height", 2);

        node.setRight(rightNode);
        node.setLeft(leftNode);
        node.updateHeight();

        assertThat(node.getHeight())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("When parent node is set, node has parent")
    void hasParent_WhenSetParent_ReturnsTrue() {
        AvlNode<Integer> dummyNode = getDummyNode();
        node.setParent(dummyNode);
        node.updateHeight();

        assertThat(node.hasParent())
                .isTrue();
        assertThat(node.isLeaf())
                .isTrue();
        assertThat(node.getHeight())
                .isEqualTo(0);
        assertThat(node.getParent())
                .isEqualTo(dummyNode);
    }

    @Test
    @DisplayName("When item is null, throws exception")
    void constructor_WhenItemIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new AvlNode<>(null));
    }

    @Test
    @DisplayName("When setting a null item, throws exception")
    void setItem_WhenItemIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.setItem(null));
    }

    @Test
    @DisplayName("When setting a non null item, changes item")
    void setItem_WhenItemIsNotNull_ChangesItem() {
        node.setItem(6);

        assertThat(node.getItem())
                .isEqualTo(6);
    }

    @Test
    @DisplayName("When setting closest node, changes closest node")
    void setClosestNode_WhenNodeIsNotNull_ChangesClosestNode() {
        AvlNode<Integer> dummyNode = getDummyNode();
        node.setClosestNode(dummyNode);

        assertThat(node.getClosestNode())
                .isEqualTo(dummyNode);
    }

    @Test
    @DisplayName("When height is set, returns same height")
    void getHeight_WhenHeightIsNotValid_ThrowsException() {
        int expectedHeight = 1000213;
        ReflectionTestUtils.setField(node, "height", expectedHeight);

        assertThat(node.getHeight())
                .isEqualTo(expectedHeight);
    }

    private AvlNode<Integer> getDummyNode() {
        return new AvlNode<>(15);
    }
}
