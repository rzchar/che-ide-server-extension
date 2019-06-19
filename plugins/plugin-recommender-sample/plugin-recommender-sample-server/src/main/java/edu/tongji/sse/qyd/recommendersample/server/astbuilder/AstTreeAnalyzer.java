package edu.tongji.sse.qyd.recommendersample.server.astbuilder;

import java.util.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class AstTreeAnalyzer {

  private String source;

  private ASTParser astParser;

  private CompilationUnit result;

  private Map<String, JSONArray> apiArrays;

  private JSONObject resultRecord;

  private long expressionNum;

  private boolean resolved;

  public AstTreeAnalyzer(String source) {
    this.apiArrays = new HashMap<>();
    this.source = source;
    this.astParser = ASTParser.newParser(AST.JLS8);
    Map options = JavaCore.getOptions();
    JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
    this.astParser.setCompilerOptions(options);
    this.astParser.setSource(source.toCharArray());
  }

  public void setSource(String source) {
    this.source = source;
    astParser.setSource(this.source.toCharArray());
  }

  public JSONObject getRecord() {
    return this.resultRecord;
  }

  public Map<String, JSONArray> reslove() {
    init();
    long startTime = Calendar.getInstance().getTimeInMillis();
    this.result = (CompilationUnit) astParser.createAST(null);
    if (result.types().isEmpty()) {
      log("Error", this.source);
      return this.apiArrays;
    }
    Object type0 = result.types().get(0);
    if (type0 instanceof TypeDeclaration) {
      TypeDeclaration td = (TypeDeclaration) type0;
      MethodDeclaration[] methods = td.getMethods();
      for (MethodDeclaration md : methods) {
        JSONArray apiArray = new JSONArray();
        if (md.getBody() == null) {
          continue;
        }
        List statements = md.getBody().statements();
        for (Object so : statements) {
          Statement statement = (Statement) so;
          resolveTreeNode(statement, apiArray);
        }
        apiArrays.put(md.getName().getFullyQualifiedName(), apiArray);
      }
      long endTime = Calendar.getInstance().getTimeInMillis();
      long duration = endTime - startTime;
      resultRecord.put("methodNum", methods.length);
      resultRecord.put("time", duration);
      resultRecord.put("expressionNum", expressionNum);
    }
    return this.apiArrays;
  }

  private void init() {
    this.expressionNum = 0;
    this.resultRecord = new JSONObject();
  }

  private void resolveTreeNode(ASTNode node, JSONArray apiArray) {
    if (node == null) {
      log("Statement", "ASTNode null");
      return;
    }
    if (node instanceof Expression) {
      // log("Expression", "Expression:" + node);
      Expression expression = (Expression) node;
      resolveExpression(expression, apiArray);
      return;
    } else if (node instanceof Statement) {

      if (node instanceof AssertStatement) {
        log("Statement", "AssertStatement");
        AssertStatement assertStatement = (AssertStatement) node;
        Expression expression = assertStatement.getExpression();
        resolveTreeNode(expression, apiArray);
        return;
      }

      // experision statement
      if (node instanceof ExpressionStatement) {
        log("Statement", "ExpressionStatement");
        ExpressionStatement expressionStatement = (ExpressionStatement) node;
        resolveTreeNode(expressionStatement.getExpression(), apiArray);
        return;
      }

      // block statement
      if (node instanceof Block) {
        log("Statement", "Block");
        Block block = (Block) node;
        List statements = block.statements();
        JSONArray blockJA = new JSONArray();

        for (Object so : statements) {
          Statement statement = (Statement) so;
          resolveTreeNode(statement, blockJA);
        }

        apiArray.put(blockJA);
        return;
      }

      // loop and if statement
      if (node instanceof DoStatement) {
        log("Statement", "DoStatement");
        DoStatement doStatement = (DoStatement) node;
        JSONArray doStatementJA = new JSONArray();

        doStatementJA.put(":do");
        resolveTreeNode(doStatement.getBody(), doStatementJA);
        doStatementJA.put(":while");
        resolveTreeNode(doStatement.getExpression(), doStatementJA);

        apiArray.put(doStatement);
        return;
      }
      if (node instanceof WhileStatement) {
        log("Statement", "WhileStatement");
        WhileStatement whileStatement = (WhileStatement) node;
        JSONArray whileStatementJA = new JSONArray();

        resolveTreeNode(whileStatement.getExpression(), whileStatementJA);
        resolveTreeNode(whileStatement.getBody(), whileStatementJA);

        apiArray.put(whileStatementJA);
        return;
      }

      if (node instanceof EnhancedForStatement) {
        log("Statement", "EnhancedForStatement");
        EnhancedForStatement enhancedForStatement = (EnhancedForStatement) node;
        JSONArray forStatementJA = new JSONArray();

        forStatementJA.put(":for");
        resolveTreeNode(enhancedForStatement.getExpression(), forStatementJA);
        resolveTreeNode(enhancedForStatement.getBody(), forStatementJA);

        apiArray.put(forStatementJA);
        return;
      }

      if (node instanceof ForStatement) {
        log("Statement", "ForStatement");
        ForStatement forStatement = (ForStatement) node;
        JSONArray forStatementJA = new JSONArray();

        forStatementJA.put(":for");
        resolveTreeNode(forStatement.getExpression(), forStatementJA);
        resolveTreeNode(forStatement.getBody(), forStatementJA);

        apiArray.put(forStatementJA);
        return;
      }
      if (node instanceof IfStatement) {
        log("Statement", "IfStatement");
        IfStatement ifStatement = (IfStatement) node;
        JSONArray ifStatementJA = new JSONArray();

        ifStatementJA.put(":if");
        resolveTreeNode(ifStatement.getExpression(), ifStatementJA);
        ifStatementJA.put(":then");
        resolveTreeNode(ifStatement.getThenStatement(), ifStatementJA);
        ifStatementJA.put(":else");
        resolveTreeNode(ifStatement.getElseStatement(), ifStatementJA);

        apiArray.put(ifStatementJA);
        return;
      }

      // reserved words
      if (node instanceof BreakStatement) {
        log("Statement", "BreakStatement ");
        apiArray.put(":break");
        return;
      }
      if (node instanceof ContinueStatement) {
        log("Statement", "ContinueStatement");
        apiArray.put(":continue");
        return;
      }

      // others
      if (node instanceof ConstructorInvocation) {
        log("Statement", "ConstructorInvocation");
        // ConstructorInvocation constructorInvocation = (ConstructorInvocation) node;
        apiArray.put(":this");
        return;
      }
      if (node instanceof EmptyStatement) {
        log("Statement", "EmptyStatement");
        return;
      }
      if (node instanceof ReturnStatement) {
        log("Statement", "ReturnStatement");
        ReturnStatement returnStatement = (ReturnStatement) node;
        JSONArray returnStatementJA = new JSONArray();

        apiArray.put(":return");
        resolveTreeNode(returnStatement.getExpression(), returnStatementJA);

        apiArray.put(returnStatementJA);
        return;
      }
      if (node instanceof LabeledStatement) {
        log("Statement", "LabeledStatement");
        LabeledStatement labeledStatement = (LabeledStatement) node;

        apiArray.put(":label:" + labeledStatement.getLabel().getIdentifier());
        resolveTreeNode(labeledStatement.getBody(), apiArray);

        return;
      }
      if (node instanceof SuperConstructorInvocation) {
        log("Statement", "SuperConstructorInvocation");
        SuperConstructorInvocation superConstructorInvocation = (SuperConstructorInvocation) node;

        resolveTreeNode(superConstructorInvocation.getExpression(), apiArray);

        apiArray.put(":superConstructor");
        return;
      }

      // Switch
      if (node instanceof SwitchStatement) {
        log("Statement", "SwitchStatement");
        SwitchStatement switchStatement = (SwitchStatement) node;
        JSONArray switchStatementJA = new JSONArray();

        switchStatementJA.put(":switch");
        resolveTreeNode(switchStatement.getExpression(), switchStatementJA);
        for (Object so : switchStatement.statements()) {
          if (so instanceof Statement || so instanceof Expression) {
            ASTNode astNode = (ASTNode) so;
            resolveTreeNode(astNode, switchStatementJA);
          }
        }

        apiArray.put(switchStatementJA);
        return;
      }
      if (node instanceof SwitchCase) {
        log("Statement", "SwitchCase");
        SwitchCase switchCase = (SwitchCase) node;
        JSONArray switchCaseJA = new JSONArray();

        switchCaseJA.put(":case");
        resolveTreeNode(switchCase.getExpression(), switchCaseJA);

        apiArray.put(switchCaseJA);
        return;
      }

      // ================
      if (node instanceof SynchronizedStatement) {
        log("Statement", "SynchronizedStatement");
        SynchronizedStatement synchronizedStatement = (SynchronizedStatement) node;
        JSONArray synchronizedStatementJA = new JSONArray();

        synchronizedStatementJA.put(":synchronized");
        resolveTreeNode(synchronizedStatement.getExpression(), synchronizedStatementJA);
        resolveTreeNode(synchronizedStatement.getBody(), synchronizedStatementJA);

        apiArray.put(synchronizedStatementJA);
        return;
      }

      if (node instanceof TryStatement) {
        log("Statement", "TryStatement");
        TryStatement tryStatement = (TryStatement) node;
        JSONArray tryStatementJA = new JSONArray();

        tryStatementJA.put(":try");
        resolveTreeNode(tryStatement.getBody(), apiArray);
        for (Object to : tryStatement.catchClauses()) {
          if (to instanceof ASTNode) {
            resolveTreeNode((ASTNode) to, apiArray);
          }
        }

        tryStatementJA.put(":finally");
        resolveTreeNode(tryStatement.getFinally(), apiArray);

        return;
      }
      if (node instanceof ThrowStatement) {
        log("Statement", "ThrowStatement");
        ThrowStatement throwStatement = (ThrowStatement) node;
        JSONArray throwStatementJA = new JSONArray();

        throwStatementJA.put(":throw");
        resolveTreeNode(throwStatement.getExpression(), throwStatementJA);

        apiArray.put(throwStatementJA);
        return;
      }

      if (node instanceof TypeDeclarationStatement) {
        log("Statement", "TypeDeclarationStatement");
        TypeDeclarationStatement typeDeclarationStatement = (TypeDeclarationStatement) node;
        apiArray.put(":type:" + typeDeclarationStatement.getDeclaration().getName());
        return;
      }
      if (node instanceof VariableDeclarationStatement) {
        log("Statement", "VariableDeclarationStatement");
        VariableDeclarationStatement variableDeclarationStatement =
            (VariableDeclarationStatement) node;
        for (Object fo : variableDeclarationStatement.fragments()) {
          VariableDeclarationFragment fragment = (VariableDeclarationFragment) fo;
          resolveTreeNode(fragment.getInitializer(), apiArray);
        }
        return;
      }
    }
  }

  private void resolveExpression(Expression expression, JSONArray apiArray) {
    if (expression == null) {
      log("Expression", "null expression");
      return;
    }
    log("Expression", "expression type: " + expression.getClass().getSimpleName());
    log("Expression", "expression: " + expression);
    this.expressionNum += 1;
    if (expression instanceof PrefixExpression) {
      PrefixExpression prefixExpression = (PrefixExpression) expression;
      resolveExpression(prefixExpression.getOperand(), apiArray);
      apiArray.put(prefixExpression.getOperator());
      return;
    }
    if (expression instanceof PostfixExpression) {
      PostfixExpression postfixExpression = (PostfixExpression) expression;
      apiArray.put(postfixExpression.getOperator());
      resolveExpression(postfixExpression.getOperand(), apiArray);
      return;
    }
    if (expression instanceof InfixExpression) {
      InfixExpression infixExpression = (InfixExpression) expression;
      resolveExpression(infixExpression.getLeftOperand(), apiArray);
      resolveExpression(infixExpression.getRightOperand(), apiArray);
      apiArray.put(infixExpression.getOperator().toString());
      return;
    }
    if (expression instanceof MethodInvocation) {
      MethodInvocation methodInvocation = (MethodInvocation) expression;
      Expression methodInvocationExpression = methodInvocation.getExpression();
      if (methodInvocationExpression != null) {
        log("InvocationExpressionType", methodInvocationExpression.getClass().toString());
        if (methodInvocationExpression instanceof SimpleName) {
          SimpleName sn = (SimpleName) methodInvocationExpression;
          log("InvocationExpressionType", "identifier: " + sn.getIdentifier());
          IBinding binding = sn.resolveBinding();
        }
      }
      resolveExpression(methodInvocation.getExpression(), apiArray);
      List arguments = methodInvocation.arguments();
      for (Object ao : arguments) {
        if (ao instanceof Expression) {
          resolveExpression((Expression) ao, apiArray);
        }
      }

      apiArray.put(methodInvocation.getName());
      log("MethodInvocation", "expression: " + methodInvocation.getExpression());
      log("MethodInvocation", "name: " + methodInvocation.getName());
      return;
    }
    if (expression instanceof ClassInstanceCreation) {
      ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
      Type type = classInstanceCreation.getType();
      log("ClassInstanceCreation", expression.toString());
      log("ClassInstanceCreation", type.getClass().getName());
      log("ClassInstanceCreation", type.toString());

      List arguments = classInstanceCreation.arguments();
      for (Object ao : arguments) {
        if (ao instanceof Expression) {
          resolveExpression((Expression) ao, apiArray);
        }
      }
      apiArray.put(":new:" + type.toString());
      return;
    }
    if (expression instanceof ArrayAccess) {
      ArrayAccess arrayAccess = (ArrayAccess) expression;
      resolveExpression(arrayAccess.getArray(), apiArray);
      resolveExpression(arrayAccess.getIndex(), apiArray);
      return;
    }
    if (expression instanceof ParenthesizedExpression) {
      ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) expression;
      resolveExpression(parenthesizedExpression.getExpression(), apiArray);
      return;
    }
    if (expression instanceof ThisExpression) {
      apiArray.put(":this");
      return;
    }
    if (expression instanceof SuperMethodInvocation) {
      SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation) expression;
      apiArray.put(":super");
      List arguments = superMethodInvocation.arguments();
      for (Object ao : arguments) {
        if (ao instanceof Expression) {
          resolveExpression((Expression) ao, apiArray);
        }
      }
      apiArray.put(superMethodInvocation.getName());
      return;
    }
    if (expression instanceof Name) {
      Name name = (Name) expression;
      apiArray.put(name.getFullyQualifiedName());
      return;
    }
    if (expression instanceof CastExpression) {
      CastExpression castExpression = (CastExpression) expression;
      resolveExpression(castExpression.getExpression(), apiArray);
      apiArray.put(castExpression.getType());
      return;
    }
    if (expression instanceof Assignment) {
      Assignment assignment = (Assignment) expression;
      resolveExpression(assignment.getLeftHandSide(), apiArray);
      apiArray.put(assignment.getOperator().toString());
      resolveExpression(assignment.getRightHandSide(), apiArray);
      return;
    }
    if (expression instanceof LambdaExpression) {
      LambdaExpression lambdaExpression = (LambdaExpression) expression;
      resolveTreeNode(lambdaExpression.getBody(), apiArray);
      return;
    }
    if (expression instanceof MethodReference) {
      MethodReference methodReference = (MethodReference) expression;
      apiArray.put(methodReference.toString());
      return;
    }
    if (expression instanceof InstanceofExpression) {
      InstanceofExpression instanceofExpression = (InstanceofExpression) expression;
      resolveExpression(instanceofExpression.getLeftOperand(), apiArray);
      apiArray.put(instanceofExpression.toString());
      return;
    }
    if (expression instanceof ConditionalExpression) {
      ConditionalExpression conditionalExpression = (ConditionalExpression) expression;
      resolveExpression(conditionalExpression.getExpression(), apiArray);
      resolveExpression(conditionalExpression.getElseExpression(), apiArray);
      resolveExpression(conditionalExpression.getThenExpression(), apiArray);
      return;
    }
    if (expression instanceof ArrayCreation) {
      ArrayCreation arrayCreation = (ArrayCreation) expression;

      for (Object ob : arrayCreation.dimensions()) {
        if (ob instanceof ASTNode) {
          resolveTreeNode((ASTNode) ob, apiArray);
        }
      }

      return;
    }
    if (expression instanceof ArrayInitializer) {
      ArrayInitializer arrayInitializer = (ArrayInitializer) expression;
      for (Object ob : arrayInitializer.expressions()) {
        if (ob instanceof Exception) {
          resolveExpression((Expression) ob, apiArray);
        }
      }
      return;
    }
    if (expression instanceof Annotation
        || expression instanceof NullLiteral
        || expression instanceof NumberLiteral
        || expression instanceof BooleanLiteral
        || expression instanceof StringLiteral
        || expression instanceof TypeLiteral
        || expression instanceof CharacterLiteral
        || expression instanceof FieldAccess) {
      return;
    }

    log("Error", expression.getClass() + " is undealed.\n" + expression);
  }

  private Set<String> labelSet =
      new HashSet<String>() {
        {
          //		   add("Expression");
          //		   add("MethodInvocation");
          //		   add("Statement");
          //		  add("InvocationExpressionType");
          //		  add("ClassInstanceCreation");

          add("Error");
          add("Time");
          add("");
          add("File");
        }
      };

  private void log(String label, String str) {
    if (labelSet.contains(label)) {
      System.out.println(label + " " + str);
    }
  }
}
