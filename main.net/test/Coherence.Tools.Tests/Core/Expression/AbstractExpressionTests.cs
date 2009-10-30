using System;
using System.Collections;
using NUnit.Framework;
using Seovic.Coherence.Test.Objects;

namespace Seovic.Coherence.Core.Expression
{
    // TODO: integrate with AbstractExpressionServerTests
    [TestFixture]
    public abstract class AbstractExpressionTests
    {
        protected abstract IExpression CreateExpression(string expression);
        protected abstract string GetLanguage();

        [Test]
        public void TestLiteralExpression()
        {
            IExpression exp = CreateExpression("5 + 5");
            Assert.AreEqual(10, exp.Evaluate(null));
        }

        [Test]
        public void TestSimplePropertyExpression()
        {
            Person      person = new Person(1L, "Homer");
            IExpression exp    = CreateExpression("name");
            Assert.AreEqual("Homer", exp.Evaluate(person));
        }

        [Test]
        public void TestNestedPropertyExpression()
        {
            Person      person = new Person(1L, "Homer", DateTime.Now,
                                       new Address("111 Main St", "Springfield", "USA"));
            IExpression exp    = CreateExpression("address.city");
            Assert.AreEqual("Springfield", exp.Evaluate(person));
        }

        [Test]
        public void TestExpressionWithVariables()
        {
            IDictionary vars = new Hashtable();
            vars["x"] = 5;
            vars["y"] = 5;

            IExpression exp = CreateExpression("x * y");
            Assert.AreEqual(25, exp.Evaluate(null, vars));
        }

        [Test]
        public void TestExpressionWithTargetAndVariables()
        {
            Person      person = new Person(1L, "Homer");
            IDictionary vars   = new Hashtable();
            vars["lastName"] = "Simpson";

            IExpression exp = CreateExpression("name + ' ' + lastName");
            Assert.AreEqual("Homer Simpson", exp.Evaluate(person, vars));
        }
    }
}