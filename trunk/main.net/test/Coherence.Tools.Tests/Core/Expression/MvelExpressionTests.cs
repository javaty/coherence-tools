namespace Seovic.Core.Expression
{
    /// <summary>
    /// MvelExpression tests.
    /// </summary>
    public class MvelExpressionTests : AbstractExpressionServerTests
    {
        protected override IExpression CreateExpression(string expression)
        {
            return new MvelExpression(expression);
        }

        protected override string GetLanguage()
        {
            return "MVEL";
        }
    }
}
