using System;
using System.Collections;
using NUnit.Framework;
using Seovic.Coherence.Core.Extractor;
using Seovic.Coherence.Test.Objects;
using Tangosol.Net;
using Tangosol.Util.Processor;

namespace Seovic.Coherence.Core.Expression
{
    [TestFixture]
    public abstract class AbstractExpressionServerTests
    {

        [SetUp]
        public void SetUp()
        {
            _cache.Clear();
        }
        
        protected abstract IExpression CreateExpression(String expression);
        protected abstract String      GetLanguage();

        [Test]
        public void TestSimplePropertyExpression()
        {
            Person     person = new Person(1L, "Homer");
            Store(person.Id, person);
            IExpression exp = CreateExpression("name");
            Assert.AreEqual("Homer", Evaluate(exp, person.Id, null));
        }

        [Test]
        public void TestNestedPropertyExpression()
        {
            Person     person = new Person(1L, "Homer", DateTime.Now,
                                       new Address("111 Main St", "Springfield", "USA"));
            Store(person.Id, person);
            IExpression exp   = CreateExpression("address.city");
            Assert.AreEqual("Springfield", Evaluate(exp, person.Id, null));
        }

        // TODO: ExpressionExtractor needs to be able to store context map
//        [Test]
//        public void TestExpressionWithTargetAndVariables()
//        {
//            Person person = new Person(1L, "Homer");
//            IDictionary vars = new Hashtable();
//            vars["lastName"] = "Simpson";
//            Store(person.Id, person);
//            IExpression exp = CreateExpression("name + ' ' + lastName");
//            Assert.AreEqual("Homer Simpson", Evaluate(exp, person.Id, vars));
//        }

        protected virtual void Store(object key, object value)
        {
            _cache.Insert(key, value);
        }

        protected virtual object Evaluate(IExpression exp, object key, IDictionary context)
        {
            ExtractorProcessor agent = new ExtractorProcessor(new ExpressionExtractor(exp));
            return _cache.Invoke(key, agent);
        }

        private INamedCache _cache = CacheFactory.GetCache("expression-test-cache");
    }
}
